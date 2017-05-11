package it.wirge.whishlistmaker;


import it.dsgroup.demandware.models.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * wirge
 * whishlistmaker:
 * Created by enricopetrelli on 11/05/17.
 */
public class Runner{

    public static void main(String[] saParams){

        Logger logger = Logger.getLogger(Runner.class);
        logger.info("Start");

        ProductLists productLists = new ProductLists();

        BufferedReader br = null;
        FileReader fr = null;

        try{
            ClassLoader classLoader = Runner.class.getClassLoader();
            File file = new File(classLoader.getResource("wishlist_aligned.csv").getFile());
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] saLine = sCurrentLine.split(";");

                if(saLine[1].equals("#N/D")){
                    continue;
                }

                ProductList productList = new ProductList();
                productList.setType(SimpleTypeProductListType.WISH_LIST);
                productList.setPublic(false);

                // owner
                Owner owner = new Owner();
                owner.setCustomerNo(saLine[1]);
                owner.setEmail(saLine[0]);
                productList.setOwner(owner);
                productList.setItems(new Items());

                boolean bContinue = false;
                // products:
                for(int i=2; i<saLine.length; i++){
                    if(saLine[i].trim().length()==0){
                        bContinue=true;
                        break;
                    }

                    if(i > 50){
                        bContinue=true;
                        logger.warn("User " + owner.getEmail() + " has a whishlist bigger than 50.");
                        break;
                    }

                    ProductItem productItem = new ProductItem();
                    productItem.setProductId(saLine[i]);
                    productItem.setQuantity(BigDecimal.ONE);
                    productItem.setPriority(0);
                    productItem.setPublic(false);
                    Purchases purchases = new Purchases();
                    productItem.setPurchases(purchases);

                    productList.getItems().getProductItem().add(productItem);
                }
                if(bContinue){
                    continue;
                }
                productLists.getProductList().add(productList);


            }
            logger.info("Writing to " + file.getParent() + "/" + "productLists.xml");
            XmlMarshaler.marshall(file.getParent() + "/" + "productLists.xml", productLists);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{

            try{

                if(br != null) br.close();

                if(fr != null) fr.close();

            }
            catch(IOException ex){

                ex.printStackTrace();

            }
        }

        // Reads The file



        logger.info("End");
    }

}
