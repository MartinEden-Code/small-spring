package cn.bugstack.springframework.beans.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongxingyi
 * @description
 * @date 2022/3/17 10:36
 */
public interface Recource {
    InputStream getInputStream() throws IOException;
}
