package tokyo.northside.omegawiki;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class SyntransParserTest {

    @Test
    void parseMeaningTest() throws IOException {
        String json = "{\"1.\":{\"langid\":\"84\",\"lang\":\"Bulgarian\",\"e\":\"\\u0430\\u0437\\u0431\\u0443\\u043a\\u0430\",\"im\":\"1\"}}";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Map.Entry<String, JsonNode> entry = node.fields().next();
        String key = entry.getKey();
        assertEquals(key, "1.");
        JsonNode meaning = entry.getValue();
        OmegawikiMeaning omegawikiMeaning = mapper.readValue(meaning.traverse(), OmegawikiMeaning.class);
        assertEquals(omegawikiMeaning.toString(), "{langid='84', lang='Bulgarian', e='\u0430\u0437\u0431\u0443\u043a\u0430', im='1'}");
    }

    @Test
    void parseSyntransTest() throws IOException{
        InputStream resource = SyntransParser.class.getClassLoader().getResourceAsStream("syntrans_result.json");
        String json = Files.readFile(resource);
        SyntransParser parser = new SyntransParser();
        parser.parse(json);
        List<OmegawikiMeaning> meanings = parser.getMeanings();
        assertEquals(meanings.get(0).getE(), "азбука");
    }
}