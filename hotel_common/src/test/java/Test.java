import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.common.security.JwtUtil;

/**
 * @author bhj
 */
public class Test {


    @org.junit.Test
    public void testJwt() throws InterruptedException {
        System.out.println("second:" + String.valueOf(System.currentTimeMillis() / 1000).substring(4));
    }

}
