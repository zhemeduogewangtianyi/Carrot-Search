package com.carrot.sec.operations.test;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.store.FSDirectory;

public class TestCreateIndex {
//    private static final String path = "d://temp/student";
    private static final String path = "/Users/wty/Downloads/temp/student";

    public TestCreateIndex() {
    }

    public static void main(String[] args) throws IOException {

    }

    public static void update() throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        Path indexPath = file.toPath();
        FSDirectory directory = FSDirectory.open(indexPath);
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig(new StandardAnalyzer()));

//        Term id = new TermQuery();

//        Doc
//        indexWriter.updateDocument();


        Query queryById = NumericDocValuesField.newSlowExactQuery("id", 3000L);


        iReader.close();
        directory.close();
    }

    public static void query() throws IOException {

        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }

        Path indexPath = file.toPath();
        FSDirectory directory = FSDirectory.open(indexPath);
        DirectoryReader iReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(iReader);
        Query queryById = NumericDocValuesField.newSlowExactQuery("id", 3000L);
        TopDocs docs = getScoreDocsByPerPage(1, 10, indexSearcher, queryById);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        System.out.println("所有的数据总数为：" + docs.totalHits);
        System.out.println("本页查询到的总数为：" + scoreDocs.length);
        ScoreDoc[] var9 = scoreDocs;
        int var10 = scoreDocs.length;

        for(int var11 = 0; var11 < var10; ++var11) {
            ScoreDoc hit = var9[var11];
            Document hitDoc = indexSearcher.doc(hit.doc);
            List<IndexableField> fields = hitDoc.getFields();
            Iterator var15 = fields.iterator();

            while(var15.hasNext()) {
                IndexableField idxField = (IndexableField)var15.next();
                System.out.println(idxField.name() + " : " + idxField.getCharSequenceValue());
            }
        }

        iReader.close();
        directory.close();

    }

    public static TopDocs getScoreDocsByPerPage(int page, int perPage, IndexSearcher searcher, Query query) throws IOException {
        if (query == null) {
            System.out.println(" Query is null return null ");
            return null;
        } else {
            ScoreDoc before = null;
            SortField sortField;
            Sort sort;
            if (page != 1) {
                sortField = new SortField("id", Type.LONG, false);
                sort = new Sort(sortField);
                TopDocs docsBefore = searcher.search(query, (page - 1) * perPage, sort, true);
                ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
                if (scoreDocs.length > 0) {
                    before = scoreDocs[scoreDocs.length - 1];
                }
            }

            sortField = new SortField("id", Type.LONG, false);
            sort = new Sort(sortField);
            TopDocs result = searcher.searchAfter(before, query, perPage, sort, false);
            return result;
        }
    }
}

