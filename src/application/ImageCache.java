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
                .maximumSize(100) //around 800mb
                .expireAfterWrite(1, TimeUnit.MINUTES) //images are deleted from cache after 1 minute
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
