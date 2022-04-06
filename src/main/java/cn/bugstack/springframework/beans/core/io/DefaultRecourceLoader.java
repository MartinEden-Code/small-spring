package cn.bugstack.springframework.beans.core.io;
import cn.hutool.core.lang.Assert;

/**
 * @author hongxingyi
 * @description 根据加载路径判断加载类型
 * @date 2022/3/17 10:50
 */
public class DefaultRecourceLoader implements RecourceLoader {

    @Override
    public Recource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return  new ClassPathRecource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        /*else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            } catch (MalformedURLException e) {
                return new FileSystemResource(location);
            }
        }*/
        return null;
    }
}
