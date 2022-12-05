package guru.qa;
import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import guru.qa.model.Glossary;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFilesTest {
    ClassLoader cl = SelenideFilesTest.class.getClassLoader();

    @Test
    void zipParseTest() throws Exception {
        try (
                InputStream resource = cl.getResourceAsStream("example/zip_test.zip");
                ZipInputStream zip = new ZipInputStream(resource);
        ) {
            ZipEntry entry;
            while ((entry = zip.getNextEntry()) != null) {
                if (entry.getName().contains(".csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zip));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(0)[0]).contains("Это");
                } else if (entry.getName().contains(".pdf")) {
                    PDF content = new PDF(zip);
                    assertThat(content.text).contains("Это PDF File");
                } else if (entry.getName().contains(".xlsx")) {
                    XLS content = new XLS(zip);
                    assertThat(content.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).contains("Это");
                    assertThat(content.excel.getSheetAt(0).getRow(0).getCell(1).getStringCellValue()).contains("XLSX");
                }
            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("src/test/resources/example/test_json.json");
            Glossary glossary = objectMapper.readValue(file,Glossary.class);
            assertThat(glossary.id).isEqualTo("321");
            assertThat(glossary.name).isEqualTo("Roman Vasin");
            assertThat(glossary.telephone).isEqualTo("79200000000");
            assertThat(glossary.pets).contains("cat","dog");
            assertThat(glossary.address.street).isEqualTo("main");
            assertThat(glossary.address.town).isEqualTo("Moscow");
            assertThat(glossary.address.postcode).isEqualTo("123456");
    }
}