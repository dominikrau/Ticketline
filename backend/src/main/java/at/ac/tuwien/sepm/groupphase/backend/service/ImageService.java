package at.ac.tuwien.sepm.groupphase.backend.service;


public interface ImageService {

    /**
     * Saves an image in the /public/uploads/images directory with a unique filename.
     * @param base64Uri the base64 encoded image
     * @return the filename of the created image
     */
    String save(String base64Uri);

    /**
     * Delete an image by its filename
     * @param fileName the file name of the image
     * @return true if deleted, false otherwise
     */
    boolean deleteByFileName(String fileName);
}
