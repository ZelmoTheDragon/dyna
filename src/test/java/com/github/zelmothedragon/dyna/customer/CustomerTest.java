package com.github.zelmothedragon.dyna.customer;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test Unitaire sur une entité persistante.
 *
 * @author MOSELLE Maxime
 */
@RunWith(Arquillian.class)
public class CustomerTest {

    @Inject
    private Customer instance;

    /**
     * Constructeur par défaut. Requis pour le fonctionnement de l'environnement
     * de test.
     */
    public CustomerTest() {
        // RAS
    }

    /**
     * Construire une archive à déployer sur le serveur d'application embarqué.
     * Cette archive comporte les classes et la configuration nécessaire pour
     * l'exécution des tests de cette classe.
     *
     * @return Archive web à déployer
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class, "customer.war")
                .addAsWebInfResource("beans.xml")
                .addAsWebInfResource("faces-config.xml")
                .addAsWebInfResource("web.xml")
                .addAsWebInfResource("payara-web.xml")
                .addAsWebInfResource("payara-resources.xml")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, "com.github.zelmothedragon.dyna");
    }

    /**
     * Tester l'injection CDI d'une entité.
     */
    @Test
    public void injectEntity() {
        Assert.assertNotNull(instance);
    }
}
