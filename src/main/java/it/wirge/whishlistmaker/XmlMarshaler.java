package it.wirge.whishlistmaker;

import it.dsgroup.demandware.models.ProductLists;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * wirge
 * whishlistmaker:
 * Created by enricopetrelli on 11/05/17.
 */
public class XmlMarshaler{

    public static <T> T unmarshall(String filePath, Class<T> t) throws JAXBException{
        JAXBContext jc = JAXBContext.newInstance(t);

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File(filePath);

        T objs = (T) unmarshaller.unmarshal(xml);
        return objs;
    }

    public static void marshall(String sFile, ProductLists productLists){
        try{
            File file = new File(sFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductLists.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(productLists, file);
            //jaxbMarshaller.marshal(pricebooks, System.out);

        }
        catch(JAXBException e){
            e.printStackTrace();
        }

    }
}
