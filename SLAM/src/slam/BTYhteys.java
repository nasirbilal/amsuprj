package slam;

/**
 * @@brief Bluetooth-yhteyden rajapinta.
 * @author L
 */
public interface BTYhteys {
    /// @brief Lähettää paketin yhteyden yli ja palauttaa saadun vastauksen.
    ///
    /// Bluetooth-yhteyden yli lähetetään annettu paketti. Vastausta
    /// kuunnellaan ja vastaanotettu paketti palautetaan kutsujalle. Jos
    /// lähetys tai vastaanotto kestävät kauemmin kuin annetun ajan verran,
    /// peruutetaan toimeksianto.
    ///
    /// @param paketti Lähetettävä paketti.
    /// @param odotusAikaMs Odotettava aika (ms) enne yhteyden katkaisemista.
    /// @return Vastaanotettu paketti tai null jos sattui virhe.

    /**
     * Palauta robotin yksilöivä numeerinen tunnus.
     * @return 
     */
    public int getRoboID();
    
    /**
     * 
     * @param paketti
     * @param odotusAikaMs
     * @return
     */
    public BTPaketti lahetaJaVastaanota(BTPaketti paketti, int odotusAikaMs);

    /// @brief Tuhoaa vanhan yhteyden ja luo sen uudestaan alusta.
    /**
     * 
     */
    public void uudelleenKaynnista();
    
    /**
     * 
     * @return
     */
    public BTPaketti annaOletusPaketti();
}
