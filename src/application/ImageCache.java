package application;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import javafx.scene.image.Image;
import javafx.util.Pair;

class ImageCache{
    private static LoadingCache<Pair<String,Integer>, Image> imgCache;

    static {
        imgCache = CacheBuilder.newBuilder()
                .maximumSize(2000) //width*height*3 = 720*576*3 ~ 1.18mb; 1.18*2000 = 2.3GB
                .expireAfterWrite(5, TimeUnit.MINUTES) //images are deleted from cache after 5 minutes
                .build(
                        new CacheLoader<Pair<String,Integer>, Image>() {
                            @Override
                            public Image load(Pair<String,Integer> key) throws Exception {
                                switch (key.getKey()){
                                    case "mix": return new Image(getClass().getResourceAsStream("/img/mix/mix-"+key.getValue()+".jpg"));
                                    case "eat": return new Image(getClass().getResourceAsStream("/img/eat/eat-"+key.getValue()+".jpg"));
                                    default: return new Image(getClass().getResourceAsStream("/img/go/go-"+key.getValue()+".jpg"));
                                }
                            }
                        }
                );
    }

    static Image get(Pair<String,Integer> key) throws ExecutionException {
        return imgCache.get(key);
    }
}
