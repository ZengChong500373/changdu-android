package org.linyi.base.utils.help;

import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.safety.EncryptionUtil;

import java.net.URLEncoder;
import java.util.*;

/**
 * ClassName:SignUtils
 * Function: 签名用的工具箱
 * Date:     2014-6-27 下午3:22:33
 *
 * @author
 */
public class SignUtils {

    /**
     * <一句话功能简述>
     * <功能详细描述>验证返回参数
     *
     * @param params
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String geneSign(Map<String, Object> params, String key) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString() + "&key=" + key;
        String signReceive = EncryptionUtil.encodeMD5ToString(preStr);
        LogUtil.d("====================  签名  前:" +  preStr);
        LogUtil.d("====================  签名  后:" + signReceive);
        return signReceive;
    }

    /**
     * 过滤参数
     *
     * @param sArray
     * @return
     * @author
     */
    public static Map<String, Object> paraFilter(Map<String, Object> sArray) {
        Map<String, Object> result = new HashMap<String, Object>(sArray.size());
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key).toString();
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>将map转成String
     *
     * @param payParams
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String payParamsToString(Map<String, Object> payParams) {
        return payParamsToString(payParams, false);
    }

    public static String payParamsToString(Map<String, Object> payParams, boolean encoding) {
        return payParamsToString(new StringBuilder(), payParams, encoding);
    }

    /**
     * @param payParams
     * @return
     * @author
     */
    public static String payParamsToString(StringBuilder sb, Map<String, Object> payParams, boolean encoding) {
        buildPayParams(sb, payParams, encoding);
        return sb.toString();
    }

    /**
     * @param payParams
     * @return
     * @author
     */
    public static void

    buildPayParams(StringBuilder sb, Map<String, Object> payParams, boolean encoding) {
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            sb.append(key).append("=");
            if (encoding) {
                sb.append(urlEncode(payParams.get(key).toString()));
            } else {
                sb.append(payParams.get(key));
            }
            sb.append("&");
        }
        sb.setLength(sb.length() - 1);
    }

    /**
     * 功能描述：WPA H5组建加密参数 啊哈哈
     *
     * @param sb
     * @param payParams
     * @param encoding
     * @return void
     * 2017年8月18日 下午8:01:26
     * @author:zhangyong
     */
    public static void wpaBuildPayParams(StringBuilder sb, Map<String, Object> payParams, boolean encoding) {
        List<String> keys = new ArrayList<String>(payParams.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            //sb.append(key).append("=");
            if (encoding) {
                sb.append(urlEncode(payParams.get(key).toString()));
            } else {
                sb.append(payParams.get(key));
            }
            //sb.append("&");
        }
        sb.setLength(sb.length());
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Throwable e) {
            return str;
        }
    }


//    public static Element readerXml(String body, String encode) throws DocumentException {
//        SAXReader reader = new SAXReader(false);
//        InputSource source = new InputSource(new StringReader(body));
//        source.setEncoding(encode);
//        Document doc = reader.read(source);
//        Element element = doc.getRootElement();
//        return element;
//    }

}

