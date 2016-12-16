package application;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.scene.image.Image;

class ImageCache{
    private static LoadingCache<Integer, Image> imgCache;

    static {
        imgCache = CacheBuilder.newBuilder()
                .maximumSize(2000) //width*height*3 = 720*576*3 ~ 1.18mb; 1.18*2000 = 2.3GB
                .expireAfterWrite(5, TimeUnit.MINUTES) //images are deleted from cache after 5 minutes
                .build(
                        new CacheLoader<Integer, Image>() {
                            @Override
                            public Image load(Integer id) throws Exception {
                                return new Image(getClass().getResourceAsStream("/img/image-"+id+".jpg"));
                            }
                        }
                );
    }

    static Image get(int id) throws ExecutionException {
        return imgCache.get(id);
    }
}
