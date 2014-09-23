package si.dlabs.gradle.task
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created by blazsolar on 16/09/14.
 */
class UploadTask extends DefaultTask {

    String accessKey;

    String secretKey;

    String bucket;

    File file;

    String keyPrefix = ""

    private AmazonS3 mAmazonS3;

    @TaskAction
    public void upload() {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        mAmazonS3 = new AmazonS3Client(credentials);

        if (file.isDirectory()) {
            uploadDir("", file)
        } else {
            uploadFile("", file)
        }

    }

    private void uploadFile(String path, File file) {
        mAmazonS3.putObject(new PutObjectRequest(
                bucket, keyPrefix + path + file.getName(), file
        ));
    }

    private void uploadDir(String path, File dir) {
        path += dir.getName() + "/";

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                uploadDir(path, file)
            } else {
                uploadFile(path, file)
            }
        }
    }

}
