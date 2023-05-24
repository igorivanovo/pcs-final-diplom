import com.google.gson.JsonObject;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pdfName", pdfName);
        jsonObject.addProperty("page", page);
        jsonObject.addProperty("count", page);
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    @Override
    public int compareTo(PageEntry o) {
        return Integer.compare(o.getCount(), this.getCount());
    }

    @Override
    public String toString() {
        return "PageEntry{" + "pdf=" + pdfName + ", page=" + page + ", count" + count + "}";
    }


    @Override
    public int hashCode() {
        int result = pdfName != null ? pdfName.hashCode() : 0;
        result = result + 38 * page;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        PageEntry o = (PageEntry) obj;
        return pdfName.equals(o.pdfName) && page == o.page;
    }
}



