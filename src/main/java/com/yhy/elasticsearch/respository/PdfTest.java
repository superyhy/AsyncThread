package com.yhy.elasticsearch.respository;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.similarity.ScriptedSimilarity;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class PdfTest {
    public static void main(String[] args) throws Exception {
        Document document = new Document(PageSize.A4);

        //新建一个pdf文件
        //ByteArrayOutputStream outputStream = null;

        PdfWriter writer= PdfWriter.getInstance(document, new FileOutputStream("E:/image/out/test.pdf"));

        document.open();
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //设置表单名称
        Font formNameFont = new Font(baseFont, 16, Font.NORMAL);
        Paragraph formName = new Paragraph("签名测试全", formNameFont);
        formName.setAlignment(Element.ALIGN_LEFT);
        document.add(formName);

        //设置记录快照二维码
        Image qrCodeImg = Image.getInstance("E:/image/waterMark/waterMark.jpg");
        qrCodeImg.setAlignment(Element.ALIGN_RIGHT);
        qrCodeImg.scaleAbsolute(100, 100);
        document.add(qrCodeImg);
        document.close();
        writer.close();
    }

}
