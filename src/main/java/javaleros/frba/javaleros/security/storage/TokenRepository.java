package javaleros.frba.javaleros.security.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class TokenRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String save(String usuario){
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuid,usuario, Duration.ofHours(1));
        return uuid;
    }

    public void delete(String token){
        redisTemplate.opsForValue().getAndDelete(token);
    }
    public String findBytoken(String id) {
        return redisTemplate.opsForValue()
                .get(id);
    }
}
