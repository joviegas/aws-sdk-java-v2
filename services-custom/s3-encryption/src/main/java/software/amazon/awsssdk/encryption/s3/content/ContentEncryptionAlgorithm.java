package software.amazon.awsssdk.encryption.s3.content;

public interface ContentEncryptionAlgorithm {
    ContentEncryptionAlgorithm AES_GCM = null; // "AES/GCM/NoPadding"
    ContentEncryptionAlgorithm AES_CBC = null; // "AES/CBC/PKCS5Padding"

    @Deprecated
    ContentEncryptionAlgorithm AES_CTR = null; // "AES/CTR/NoPadding"

    @Deprecated
    ContentEncryptionAlgorithm NOT_ENCRYPTED = null;

    ContentEncryptor createContentEncryptor();
}