import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@AllArgsConstructor
public class ConfigController {
    File file;

    public void saveProperties(Properties prop) throws IOException {
        FileOutputStream fr = new FileOutputStream(file);
        prop.store(fr, "Properties");
        fr.close();
    }

    public Properties loadProperties() throws IOException
    {
        FileInputStream fi=new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(fi);
        fi.close();
        return prop;
    }
}
