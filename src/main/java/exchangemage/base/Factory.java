package exchangemage.base;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface Factory<T> {
    ObjectMapper mapper = new ObjectMapper();

    interface FactoryNode<T> {
        T createFromJson(JsonNode sourceJson);

        String val();
    }

    class SourceFormatException extends IllegalArgumentException {
        public SourceFormatException(String message) {super(message);}
    }

    default T createFromPath(String sourcePath) {
        return createFromFile(new File(sourcePath));
    }

    default T createFromFile(File sourceFile) {
        try {
            return createFromJson(mapper.readTree(sourceFile));
        } catch (IOException e) {
            throw new RuntimeException(String.format(
                    "Failed to load trigger definition from %s due to the following error:\n%s",
                    sourceFile.getAbsolutePath(), e.getMessage()
            ), e);
        }
    }

    T createFromJson(JsonNode sourceJson);
}
