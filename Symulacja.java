import SymulatorEpidemii.Simulation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class Symulacja {

    public static void main(String[] args) {
        String defaultFn = "default.properties";
        String settingsFn = "simulation-conf.xml";
        Properties defaultProps = new Properties();
        Properties settingsProps = new Properties();
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        try (Reader r = Channels.newReader(Channels.newChannel(contextLoader.getResourceAsStream(defaultFn)),
                    StandardCharsets.UTF_8.name())) {
            defaultProps.load(r);
        } catch (MalformedInputException e) {
            System.err.printf("%s nie jest tekstowy%n", defaultFn);
            return;
        } catch (NullPointerException|IOException e) {
            System.err.printf("Brak pliku %s%n", defaultFn);
            return;
        }
        try (InputStream is = contextLoader.getResourceAsStream(settingsFn)) {
            settingsProps.loadFromXML(is);
        } catch (InvalidPropertiesFormatException e) {
            System.err.printf("%s nie jest xml%n", settingsFn);
            return;
        } catch (NullPointerException|IOException e) {
            System.err.printf("Brak pliku %s%n", settingsFn);
            return;
        }
        try {
            Simulation simulation = new Simulation(defaultProps, settingsProps);
            simulation.run();
        } catch (Simulation.PropertyException e) {
            if (e.value == null)
                System.err.printf("Brak wartości dla klucza %s%n", e.key);
            else
                System.err.printf("Niedozwolona wartość \"%s\" dla klucza %s%n", e.value, e.key);
        }
    }

}
