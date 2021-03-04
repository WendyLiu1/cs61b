package bearmaps.proj2c.server.handler.impl;

import bearmaps.proj2ab.KdTree;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.AugmentedStreetMapGraph;
import bearmaps.proj2c.server.handler.APIRouteHandler;
import spark.Request;
import spark.Response;
import bearmaps.proj2c.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static bearmaps.proj2c.utils.Constants.*;

/**
 * Handles requests from the web browser for map images. These images
 * will be rastered into one large image to be displayed to the user.
 * @author rahul, Josh Hug, _________
 */
public class RasterAPIHandler extends APIRouteHandler<Map<String, Double>, Map<String, Object>> {

    /**
     * Each raster request to the server will have the following parameters
     * as keys in the params map accessible by,
     * i.e., params.get("ullat") inside RasterAPIHandler.processRequest(). <br>
     * ullat : upper left corner latitude, <br> ullon : upper left corner longitude, <br>
     * lrlat : lower right corner latitude,<br> lrlon : lower right corner longitude <br>
     * w : user viewport window width in pixels,<br> h : user viewport height in pixels.
     **/
    private static final String[] REQUIRED_RASTER_REQUEST_PARAMS = {"ullat", "ullon", "lrlat",
            "lrlon", "w", "h"};

    /**
     * The result of rastering must be a map containing all of the
     * fields listed in the comments for RasterAPIHandler.processRequest.
     **/
    private static final String[] REQUIRED_RASTER_RESULT_PARAMS = {"render_grid", "raster_ul_lon",
            "raster_ul_lat", "raster_lr_lon", "raster_lr_lat", "depth", "query_success"};


    @Override
    protected Map<String, Double> parseRequestParams(Request request) {
        return getRequestParams(request, REQUIRED_RASTER_REQUEST_PARAMS);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param requestParams Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @param response : Not used by this function. You may ignore.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image;
     *                    can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    @Override
    public Map<String, Object> processRequest(Map<String, Double> requestParams, Response response) {
        Map<String, Object> results = new HashMap<>();
        double ulLon = requestParams.get("ullon");
        double ulLat = requestParams.get("ullat");
        double lrLon = requestParams.get("lrlon");
        double lrLat = requestParams.get("lrlat");
        double width = requestParams.get("w");
        double height = requestParams.get("h");
        if (!validInput(ulLon, ulLat, lrLon, lrLat, width, height)) {
            results = queryFail();
            return results;
        }
        int depth = RasterAPIHandler.getDepth(lrLon, ulLon, width);
        Map<Point, Integer> centerPointMap = new HashMap<>();
        KdTree depthKdTree = RasterAPIHandler.generateCenterKdTree(depth, centerPointMap);
        int nearestULTileSequence = RasterAPIHandler.findNearestTile(ulLon, ulLat, depthKdTree, depth, centerPointMap);
        int nearestLRTileSequence = RasterAPIHandler.findNearestTile(lrLon, lrLat, depthKdTree, depth, centerPointMap);
        String[][] files = RasterAPIHandler.getFileSequences(nearestULTileSequence, nearestLRTileSequence, depth);
        int startX = RasterAPIHandler.getXSequence(nearestULTileSequence, depth);
        int startY = RasterAPIHandler.getYSequence(nearestULTileSequence, depth);
        int endX = RasterAPIHandler.getXSequence(nearestLRTileSequence, depth);
        int endY = RasterAPIHandler.getYSequence(nearestLRTileSequence, depth);
        Point ul = RasterAPIHandler.getGeoPoint(startX * TILE_SIZE, startY * TILE_SIZE, depth);
        Point lr = RasterAPIHandler.getGeoPoint((endX + 1) * TILE_SIZE, (endY + 1) * TILE_SIZE, depth);
        results.put("depth", depth);
        results.put("render_grid", files);
        results.put("query_success", true);
        results.put("raster_ul_lon", ul.getX());
        results.put("raster_ul_lat", ul.getY());
        results.put("raster_lr_lon", lr.getX());
        results.put("raster_lr_lat", lr.getY());
        return results;
    }

    /**
     * Get the required depth based on LonDPP
     * @param lrLon lower right longitude
     * @param ulLon upper left longitude
     * @param width width of the given view area
     * @return closest depth of view
     */
    private static int getDepth(double lrLon, double ulLon, double width) {
        double requestedLonDPP = RasterAPIHandler.getLongitudinalDistancePerPixel(lrLon, ulLon, width);
        double[] depthLonDPP = new double[8];

        for (int i = 0; i < 8; i++) {
            depthLonDPP[i] = RasterAPIHandler.getLongitudinalDistancePerPixel(
                    ROOT_LRLON, ROOT_ULLON, TILE_SIZE * Math.pow(2, i));
        }

        // Use binary search to find the closest LonDPP
        return RasterAPIHandler.findNearestSmallerIndex(depthLonDPP, requestedLonDPP);
    }

    /**
     * Find the index of the largest value that is smaller than target,
     * if not find, return the last index
     * @param input input is in descending order
     * @param target target value
     * @return index of such element
     */
    private static int findNearestSmallerIndex(double[] input, double target) {
        int left = 0;
        int right = input.length - 1;
        while (left < right) {
            int mid = (right - left) / 2 + left;
            if (input[mid] > target) {
                left = mid + 1;
            } else if (input[mid] < target) {
                right = mid;
            } else {
                left = mid;
                break;
            }
        }
        return left;
    }

    /**
     * Build a KdTree based on depth, each point is the center coordinate of the image
     * @param depth depth of the view
     * @param centerPointMap point -> image file sequence
     * @return new KdTree containing 2 ^ 2k points
     */
    private static KdTree generateCenterKdTree(int depth, Map<Point, Integer> centerPointMap) {
        List<Point> centerPoints = new ArrayList<>();
        int numOfImagesPerDirection = (int) Math.pow(2, depth);
        int gridCenterCompensate = TILE_SIZE / 2;
        for (int y = 0; y < numOfImagesPerDirection; y++) {
            for (int x = 0; x < numOfImagesPerDirection; x++) {
                Point center = RasterAPIHandler.getGeoPoint(x * TILE_SIZE + gridCenterCompensate,
                        y * TILE_SIZE + gridCenterCompensate, depth);
                centerPoints.add(center);
                centerPointMap.put(center, RasterAPIHandler.getFileSequence(x, y, depth));
            }
        }
        KdTree kdTree = new KdTree(centerPoints);
        return kdTree;
    }

    /**
     * Convert the
     * @param geoPos1 position 1's geo coordinate(latitude/longitude)
     * @param gridPos1 position 1's grid coordinate(latitude/longitude)
     * @param geoPos2 position 2's geo coordinate(latitude/longitude)
     * @param gridPos2 position 2's grid coordinate(latitude/longitude)
     * @param targetGridPos target position's grid coordinate/latitude/longitude)
     * @return target position's geo coordinate
     */
    private static double convertGridCoordinateToGeoCoordinate(
            double geoPos1, double gridPos1,
            double geoPos2, double gridPos2,
            double targetGridPos) {
        return (geoPos1 - geoPos2) / (gridPos1 - gridPos2) * (targetGridPos - gridPos1)
                + geoPos1;
    }

    /**
     * Get the geo coordinate for x, y
     * @param x x grid coordinate
     * @param y y grid coordinate
     * @param depth depth of the view
     * @return geo center point
     */
    private static Point getGeoPoint(int x, int y, int depth){
        double edgeGridCoordinate = TILE_SIZE * Math.pow(2, depth);
        double geoX = RasterAPIHandler.convertGridCoordinateToGeoCoordinate(
                ROOT_ULLON, 0, ROOT_LRLON, edgeGridCoordinate,
                x
        );
        double geoY = RasterAPIHandler.convertGridCoordinateToGeoCoordinate(
                ROOT_ULLAT, 0, ROOT_LRLAT, edgeGridCoordinate,
                y
        );
        return new Point(geoX, geoY);
    }

    /**
     * Find which tile this point belongs to
     * @param lon longitude of the point
     * @param lat latitude of the point
     * @param kdTree kdTree of the center points
     * @param depth depth of the view
     * @param centerPointMap center point -> file sequence
     * @return file sequence
     */
    private static int findNearestTile(double lon, double lat, KdTree kdTree, int depth,
                                       Map<Point, Integer> centerPointMap) {
        Point nearest = kdTree.nearest(lon, lat);
        return centerPointMap.get(nearest);
    }

    /**
     *
     * @param ulSequence
     * @param lrSequence
     * @param depth
     * @return
     */
    private static String[][] getFileSequences(int ulSequence, int lrSequence, int depth) {
        int startX = RasterAPIHandler.getXSequence(ulSequence, depth);
        int startY = RasterAPIHandler.getYSequence(ulSequence, depth);
        int endX = RasterAPIHandler.getXSequence(lrSequence, depth);
        int endY = RasterAPIHandler.getYSequence(lrSequence, depth);

        String[][] results = new String[endY - startY + 1][];
        for (int y = startY; y <= endY; y++) {
            String[] row = new String[endX - startX + 1];
            for (int x = startX; x <= endX; x++) {
                row[x - startX] = RasterAPIHandler.getFileName(x, y, depth);
            }
            results[y - startY] = row;
        }
        return results;
    }

    /**
     * Get file name based on input
     * @param x x sequence
     * @param y y sequence
     * @param depth view depth
     * @return file name
     */
    private static String getFileName( int x, int y, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("d");
        sb.append(depth);
        sb.append("_x");
        sb.append(x);
        sb.append("_y");
        sb.append(y);
        return sb.toString();
    }

    /**
     * Get the x sequence based on fileSequence
     * @param fileSequence fileSequence
     * @param depth depth of the view
     * @return x sequence
     */
    private static int getXSequence(int fileSequence, int depth) {
        return  fileSequence % (int) Math.pow(2, depth);
    }

    /**
     * Get the y sequence based on fileSequence
     * @param fileSequence fileSequence
     * @param depth depth of the view
     * @return y sequence
     */
    private static int getYSequence(int fileSequence, int depth) {
        return fileSequence / (int) Math.pow(2, depth);
    }

    /**
     * Get the file sequence based on x and y sequences
     * fS = yS * 2^depth + xS
     * @param xSequence x sequence
     * @param ySequence y sequence
     * @param depth depth of the view
     * @return file sequence
     */
    private static int getFileSequence(int xSequence, int ySequence, int depth) {
        return ySequence * (int) Math.pow(2, depth) + xSequence;
    }
    /**
     * Given a query box or image, the LonDPP of that box or image is
     * LonDPP = (lower right longitude - upper left longitude) / width of the image (or box) in pixels
     * @param lrLon lower right longitude
     * @param ulLon upper left longitude
     * @param width width of the given box
     * @return longitudinal distance per pixel
     */
    private static double getLongitudinalDistancePerPixel(double lrLon, double ulLon, double width) {
        return (lrLon - ulLon) / width;
    }

    /**
     * Check if the given query location is outside of the map area.
     * @param ulLon upper left longitude
     * @param ulLat upper left latitude
     * @param lrLon lower right longitude
     * @param lrLat lower right latitude
     * @param width width of the view box
     * @param height height of the view box
     * @return true if input is within range, false otherwise
     */
    private static boolean validInput(
            double ulLon, double ulLat, double lrLon, double lrLat, double width, double height) {
        if (ulLon >= ROOT_LRLON || ulLat <= ROOT_LRLAT ||
                lrLon <= ROOT_ULLON || lrLat >= ROOT_ULLAT ||
                width <= 0 || height <= 0) {
            return false;
        }
        return true;
    }


    @Override
    protected Object buildJsonResponse(Map<String, Object> result) {
        boolean rasterSuccess = validateRasteredImgParams(result);

        if (rasterSuccess) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeImagesToOutputStream(result, os);
            String encodedImage = Base64.getEncoder().encodeToString(os.toByteArray());
            result.put("b64_encoded_image_data", encodedImage);
        }
        return super.buildJsonResponse(result);
    }

    private Map<String, Object> queryFail() {
        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", null);
        results.put("raster_ul_lon", 0);
        results.put("raster_ul_lat", 0);
        results.put("raster_lr_lon", 0);
        results.put("raster_lr_lat", 0);
        results.put("depth", 0);
        results.put("query_success", false);
        return results;
    }

    /**
     * Validates that Rasterer has returned a result that can be rendered.
     * @param rip : Parameters provided by the rasterer
     */
    private boolean validateRasteredImgParams(Map<String, Object> rip) {
        for (String p : REQUIRED_RASTER_RESULT_PARAMS) {
            if (!rip.containsKey(p)) {
                System.out.println("Your rastering result is missing the " + p + " field.");
                return false;
            }
        }
        if (rip.containsKey("query_success")) {
            boolean success = (boolean) rip.get("query_success");
            if (!success) {
                System.out.println("query_success was reported as a failure");
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the images corresponding to rasteredImgParams to the output stream.
     * In Spring 2016, students had to do this on their own, but in 2017,
     * we made this into provided code since it was just a bit too low level.
     */
    private  void writeImagesToOutputStream(Map<String, Object> rasteredImageParams,
                                                  ByteArrayOutputStream os) {
        String[][] renderGrid = (String[][]) rasteredImageParams.get("render_grid");
        int numVertTiles = renderGrid.length;
        int numHorizTiles = renderGrid[0].length;

        BufferedImage img = new BufferedImage(numHorizTiles * Constants.TILE_SIZE,
                numVertTiles * Constants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = img.getGraphics();
        int x = 0, y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getImage(Constants.IMG_ROOT + renderGrid[r][c]), x, y, null);
                x += Constants.TILE_SIZE;
                if (x >= img.getWidth()) {
                    x = 0;
                    y += Constants.TILE_SIZE;
                }
            }
        }

        /* If there is a route, draw it. */
        double ullon = (double) rasteredImageParams.get("raster_ul_lon"); //tiles.get(0).ulp;
        double ullat = (double) rasteredImageParams.get("raster_ul_lat"); //tiles.get(0).ulp;
        double lrlon = (double) rasteredImageParams.get("raster_lr_lon"); //tiles.get(0).ulp;
        double lrlat = (double) rasteredImageParams.get("raster_lr_lat"); //tiles.get(0).ulp;

        final double wdpp = (lrlon - ullon) / img.getWidth();
        final double hdpp = (ullat - lrlat) / img.getHeight();
        AugmentedStreetMapGraph graph = SEMANTIC_STREET_GRAPH;
        List<Long> route = ROUTE_LIST;

        if (route != null && !route.isEmpty()) {
            Graphics2D g2d = (Graphics2D) graphic;
            g2d.setColor(Constants.ROUTE_STROKE_COLOR);
            g2d.setStroke(new BasicStroke(Constants.ROUTE_STROKE_WIDTH_PX,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            route.stream().reduce((v, w) -> {
                g2d.drawLine((int) ((graph.lon(v) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(v)) * (1 / hdpp)),
                        (int) ((graph.lon(w) - ullon) * (1 / wdpp)),
                        (int) ((ullat - graph.lat(w)) * (1 / hdpp)));
                return w;
            });
        }

        rasteredImageParams.put("raster_width", img.getWidth());
        rasteredImageParams.put("raster_height", img.getHeight());

        try {
            ImageIO.write(img, "png", os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage getImage(String imgPath) {
        BufferedImage tileImg = null;
        if (tileImg == null) {
            try {
                File in = new File(imgPath);
                tileImg = ImageIO.read(in);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return tileImg;
    }
}
