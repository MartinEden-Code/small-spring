package cn.bugstack.springframework.beans.core.io;

import cn.hutool.core.lang.Assert;
import org.springframework.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author hongxingyi
 * @description TODO
 * @date 2022/3/17 10:42
 */
public class ClassPathRecource implements Recource {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathRecource(String path) {
        this(path,null);
    }

    public ClassPathRecource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }


    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(
                    this.path + " cannot be opened because it does not exist");
        }
        return is;
    }
}
