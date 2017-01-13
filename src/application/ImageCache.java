package application;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.scene.image.Image;
import javafx.util.Pair;

class ImageCache{
    private static LoadingCache<Integer, Image> imgCache;

    static {
        imgCache = CacheBuilder.newBuilder()
                .maximumSize(2000) //width*height*3 = 720*576*3 ~ 1.18mb; 1.18*2000 = 2.3GB
                .expireAfterAccess(1, TimeUnit.MINUTES) //images are deleted from cache after 1 minute
                .build(
                        new CacheLoader<Integer, Image>() {
                            @Override
                            public Image load(Integer key) throws Exception {
                                if(key<10000) {
                                    return new Image(getClass().getResourceAsStream("/img/mix/mix-" + key + ".jpg"));
                                }else if(key<20000){
                                    return new Image(getClass().getResourceAsStream("/img/eat/eat-"+(key-10000)+".jpg"));
                                }else {
                                    return new Image(getClass().getResourceAsStream("/img/go/go-"+(key-20000)+".jpg"));
                                }
                            }
                        }
                );
    }

    static Image get(Integer key) throws ExecutionException {
        try {
            return imgCache.get(key);
        }catch(java.lang.OutOfMemoryError e){
            return null;
        }
    }
}
