public interface IReport {

    boolean startEncryption(String directory) throws Exception;

    void startDecryption() throws Exception;

    void deleteAllFiles();
}
