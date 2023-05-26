import com.google.gson.Gson;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> wordBase = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        for (File item : pdfsDir.listFiles()) {
            var doc = new PdfDocument(new PdfReader(item));
            int pageCount = doc.getNumberOfPages();
            for (int i = 1; i <= pageCount; i++) {
                var page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> wordsPage = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    wordsPage.put(word, wordsPage.getOrDefault(word, 0) + 1);
                }
                for (String word : wordsPage.keySet()) {
                    PageEntry pageEntry = new PageEntry(item.getName(), i, wordsPage.get(word));
                    if (wordBase.containsKey(word)) {
                        wordBase.get(word).add(pageEntry);
                    } else {
                        wordBase.put(word, new ArrayList<>());
                        wordBase.get(word).add(pageEntry);
                    }
                }
            }
        }
    }

    public List<String> correctString(String request) {
        List<String> correctMyList = new ArrayList<>(Arrays.asList(request.split(" ")));
        try (BufferedReader br = new BufferedReader(new FileReader("stop-ru.txt"))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (correctMyList.contains(s)) {
                    correctMyList.remove(s);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return correctMyList;
    }

    @Override
    public List<PageEntry> search(String reguest) {
        List<PageEntry> resultResponse = new ArrayList<>();
        List<String> myList = correctString(reguest.toLowerCase());
        for (String word : myList) {
            List<PageEntry> result = wordBase.get(word);
            if (result == null) {
                continue;
            }
            for (PageEntry pageEntry : result) {
                if (resultResponse.isEmpty()) {
                    resultResponse.addAll(result);
                    break;
                } else {
                    String pdf = pageEntry.getPdfName();
                    int page = pageEntry.getPage();
                    int count = pageEntry.getCount();
                    boolean noNamePage = true;
                    for (PageEntry pageEntryResponse : resultResponse) {
                        if (pageEntry.equals(pageEntryResponse) && pageEntry.getPage() == pageEntryResponse.getPage()) {
                            int countR = count + pageEntryResponse.getCount();
                            resultResponse.remove(pageEntry);
                            resultResponse.add(new PageEntry(pdf, page, countR));
                            noNamePage = false;
                            break;
                        }

                    }
                    if (noNamePage) {
                        resultResponse.add(pageEntry);
                    }

                }
            }
        }
        Collections.sort(resultResponse);
        return resultResponse;
    }
}







