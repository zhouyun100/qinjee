package com.qinjee.masterdata.service.organation.impl;


import com.qinjee.exception.ExceptionCast;
import com.qinjee.masterdata.dao.organation.PostDao;
import com.qinjee.masterdata.dao.organation.PostInstructionsDao;
import com.qinjee.masterdata.model.entity.Post;
import com.qinjee.masterdata.model.entity.PostInstructions;
import com.qinjee.masterdata.service.organation.PostInstructionsService;
import com.qinjee.model.request.UserSession;
import com.qinjee.model.response.CommonCode;
import com.qinjee.model.response.ResponseResult;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author 彭洪思
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月24日 15:07:00
 */
@Service
public class PostInstructionsImpl implements PostInstructionsService {

    @Autowired
    private PostInstructionsDao postInstructionsDao;
    @Autowired
    private PostDao postDao;

    @Override
    public ResponseResult<PostInstructions> showPostInstructions(Integer postId) {
        PostInstructions postInstructions = postInstructionsDao.getPostInstructionsByPostId(postId);
        String s = new String(postInstructions.getInstructionContent());
        return new ResponseResult<>(postInstructions);
    }



    @Override
    public ResponseResult downloadInstructions(Integer instructionId, HttpServletResponse response) {
        POIFSFileSystem poifs;
        ByteArrayInputStream bais = null;
        try {
            PostInstructions postInstructions = postInstructionsDao.selectByPrimaryKey(instructionId);
            byte[] instructionContent = postInstructions.getInstructionContent();
            String htmlContent = new String(instructionContent, "utf-8");
            Integer postId = postInstructions.getPostId();
            Post post = postDao.getPostById(postId);
            //HTML内容必须被<html><body></body></html>包装
            String html = getHtmlText(htmlContent);
            String name = URLEncoder.encode(post.getPostCode() + "#" + post.getPostName(), "UTF-8") + ".doc";
            response.setHeader("Content-Disposition", "attachment;filename=" + name);

            byte[] b = html.getBytes();
            bais = new ByteArrayInputStream(b);
            poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            //WordDocument名称不允许修改
            directory.createDocument("WordDocument", bais);
            OutputStream outputStream = response.getOutputStream();
            poifs.writeFilesystem(outputStream);

        }catch (Exception e) {
            ExceptionCast.cast(CommonCode.FILE_PARSING_EXCEPTION);
        }finally {
            IOUtils.closeQuietly(bais);
        }

        return new ResponseResult();
    }

    /**
     * 拼接成一个完整的Html
     * @param htmlContent
     * @return
     */
    private String getHtmlText(String htmlContent) {
        String htmlPrefix = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>";

        String htmlSuffix = "</body>\n" +
                "</html>";

        return htmlPrefix + htmlContent + htmlSuffix;
    }


    /**
     * 封装一个新的岗位说明对象
     * @param userSession
     * @param sheetName
     * @return
     */
    private PostInstructions packPostInstructions(UserSession userSession, String sheetName) {
        String postCode = sheetName.substring(0, sheetName.indexOf("#"));
        String postName = sheetName.substring(sheetName.indexOf("#") + 1);
        //判断文件名是否合法
        Post existPost = isExistPost(postCode, postName);
        PostInstructions postInstructions = new PostInstructions();
        postInstructions.setPostId(existPost.getPostId());
        postInstructions.setOperatorId(userSession.getArchiveId());
        postInstructions.setIsDelete((short) 0);
        return postInstructions;
    }

    /**
     * 岗位说明书word转html只支持doc
     * @param userSession
     * @param input
     * @param filename
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    private PostInstructions getPostInstructionsWord2Html(UserSession userSession, InputStream input, String filename) throws IOException, ParserConfigurationException, TransformerException {
        PostInstructions postInstructions = packPostInstructions(userSession, filename);
        //word转html存入数据库
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .newDocument());
        wordToHtmlConverter.setPicturesManager((content, pictureType, suggestedName, widthInches, heightInches) -> suggestedName);
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        outStream.close();
        byte[] bytes = outStream.toByteArray();
        String content = handlerContentToHtml(new String(bytes, "utf-8"));
        postInstructions.setInstructionContent(content.getBytes("utf-8"));
        return postInstructions;
    }

    /**
     * 把word生成的html处理只保留内容标签
     * @param content
     * @return
     */
    private String handlerContentToHtml(String content) {
        String cssText = content.substring(content.indexOf("<style type=\"text/css\">") + 24, content.indexOf("</style>"));
        String[] split = cssText.split("\n");

        int index1 = content.indexOf("<body");
        int index2 = content.indexOf(">", index1 + 4);
        String substring = content.substring(0, index2 + 1);
        String html = content.substring(substring.length() + 1, content.lastIndexOf("</body>"));

        StringBuilder sb = new StringBuilder(html);
        for (String str : split) {
            int fromIndex = 0;
            String cssName = str.substring(str.indexOf(".") + 1, str.indexOf("{"));
            String css = str.substring(str.indexOf("{") + 1, str.indexOf("}"));
            System.out.println("cssName:" + cssName + "======" + "css:" + css);
            String className = "class=\"" + cssName + "\"";

            int indexOf = sb.indexOf(className, fromIndex);

            while (indexOf > 0){
                fromIndex = indexOf + 1;
                sb.insert((indexOf + ("class=\"" + cssName + "\"").length())," style=\"" + css + "\"");
                indexOf = sb.indexOf(className, fromIndex);
            }
        }
        return sb.toString();
    }

    /**
     * 判断文件名是否合法
     * @param postCode
     * @param postName
     */
    private Post isExistPost(String postCode, String postName) {
        Post post = postDao.getPostByPostCodeAndName(postCode, postName);
        if (post == null) {
            ExceptionCast.cast(CommonCode.POST_DOES_NOT_EXIST);
        }
        return post;
    }
}
