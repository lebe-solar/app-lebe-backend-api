package de.lebe.backend.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class RequestCacheService {

    private final LoadingCache<String, Boolean> requestCache;

    public RequestCacheService() {
        requestCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<>() {
                    public Boolean load(String key) {
                        return false;
                    }
                });
    }

    public boolean isDuplicateRequest(String email, String clientIp) {
        String key = clientIp + ":" + email;
        try {
            boolean exists = requestCache.get(key);
            if (exists) {
                return true;
            } else {
                requestCache.put(key, true);
                return false;
            }
        } catch (ExecutionException e) {
            return false;
        }
    }
}