import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author bhj
 */
public class Test {

    @org.junit.Test
    public void test() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("XUST-7202-admin"));
    }
}
