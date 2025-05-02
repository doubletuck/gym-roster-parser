package com.gym.parser.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Adding tests for AP names when they differ from the short
 * and long names.
 */
public class StateTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(State.find(""), "Empty string");
        assertNull(State.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(State.find(null));
    }

    @Test
    public void findByShortName() {
        assertEquals(State.AL, State.find("AL"), "Uppercase");
        assertEquals(State.AL, State.find("al"), "Lowercase");
        assertEquals(State.AL, State.find("aL"), "Mixed case");
    }

    @Test
    public void findByLongName() {
        assertEquals(State.AL, State.find("ALABAMA"), "Uppercase");
        assertEquals(State.AL, State.find("alabama"), "Lowercase");
        assertEquals(State.AL, State.find("aLABamA"), "Mixed case");
    }

    @Test
    public void findByOtherName() {
        assertEquals(State.AL, State.find("ALA"), "Uppercase");
        assertEquals(State.AL, State.find("ala"), "Lowercase");
        assertEquals(State.AL, State.find("aLA"), "Mixed case");
        assertEquals(State.AL, State.find("Ala."), "With period");
        assertEquals(State.AL, State.find("A.l.a."), "Lots of periods");
    }

    @Test
    public void findAlabama() {
        assertEquals(State.AL, State.find("AL"), "Short Name");
        assertEquals(State.AL, State.find("Alabama"), "Long Name");
        assertEquals(State.AL, State.find("Ala"), "Other Name");
        assertEquals(State.AL, State.find("Ala."), "AP Name");
    }

    @Test
    public void findAlaska() {
        assertEquals(State.AK, State.find("AK"), "Short Name");
        assertEquals(State.AK, State.find("Alaska"), "Long Name");
    }

    @Test
    public void findArizona() {
        assertEquals(State.AZ, State.find("AZ"), "Short Name");
        assertEquals(State.AZ, State.find("Arizona"), "Long Name");
        assertEquals(State.AZ, State.find("Ariz"), "Other Name");
        assertEquals(State.AZ, State.find("Ariz."), "AP Name");
    }

    @Test
    public void findArkansas() {
        assertEquals(State.AR, State.find("AR"), "Short Name");
        assertEquals(State.AR, State.find("Arkansas"), "Long Name");
        assertEquals(State.AR, State.find("Ark"), "Other Name");
        assertEquals(State.AR, State.find("Ark."), "AP Name");
    }

    @Test
    public void findCalifornia() {
        assertEquals(State.CA, State.find("CA"), "Short Name");
        assertEquals(State.CA, State.find("California"), "Long Name");
        assertEquals(State.CA, State.find("Calif"), "Other Name");
        assertEquals(State.CA, State.find("Calif."), "AP Name");
    }

    @Test
    public void findColorado() {
        assertEquals(State.CO, State.find("CO"), "Short Name");
        assertEquals(State.CO, State.find("Colorado"), "Long Name");
        assertEquals(State.CO, State.find("Colo"), "Other Name");
        assertEquals(State.CO, State.find("Colo."), "AP Name");
    }

    @Test
    public void findConnecticut() {
        assertEquals(State.CT, State.find("CT"), "Short Name");
        assertEquals(State.CT, State.find("Connecticut"), "Long Name");
        assertEquals(State.CT, State.find("Conn"), "Other Name");
        assertEquals(State.CT, State.find("Conn."), "AP Name");
    }

    @Test
    public void findDelaware() {
        assertEquals(State.DE, State.find("DE"), "Short Name");
        assertEquals(State.DE, State.find("Delaware"), "Long Name");
        assertEquals(State.DE, State.find("Del"), "Other Name");
        assertEquals(State.DE, State.find("Del."), "AP Name");
    }

    @Test
    public void findDistrictOfColumbia() {
        assertEquals(State.DC, State.find("DC"), "Short Name");
        assertEquals(State.DC, State.find("District of Columbia"), "Long Name");
        assertEquals(State.DC, State.find("D.C."), "AP Name");
    }

    @Test
    public void findFlorida() {
        assertEquals(State.FL, State.find("FL"), "Short Name");
        assertEquals(State.FL, State.find("Florida"), "Long Name");
        assertEquals(State.FL, State.find("Fla"), "Other Name");
        assertEquals(State.FL, State.find("Fla."), "AP Name");
    }

    @Test
    public void findGeorgia() {
        assertEquals(State.GA, State.find("GA"), "Short Name");
        assertEquals(State.GA, State.find("Georgia"), "Long Name");
        assertEquals(State.GA, State.find("Ga."), "AP Name");
    }

    @Test
    public void findHawaii() {
        assertEquals(State.HI, State.find("HI"), "Short Name");
        assertEquals(State.HI, State.find("Hawaii"), "Long Name");
        assertEquals(State.HI, State.find("Hawai'i"), "Other Name");
    }

    @Test
    public void findIdaho() {
        assertEquals(State.ID, State.find("ID"), "Short Name");
        assertEquals(State.ID, State.find("Idaho"), "Long Name");
    }

    @Test
    public void findIllinois() {
        assertEquals(State.IL, State.find("IL"), "Short Name");
        assertEquals(State.IL, State.find("Illinois"), "Long Name");
        assertEquals(State.IL, State.find("Ill"), "Other Name");
        assertEquals(State.IL, State.find("Ill."), "AP Name");
    }

    @Test
    public void findIndiana() {
        assertEquals(State.IN, State.find("IN"), "Short Name");
        assertEquals(State.IN, State.find("Indiana"), "Long Name");
        assertEquals(State.IN, State.find("Ind"), "Other Name");
        assertEquals(State.IN, State.find("Ind."), "AP Name");
    }

    @Test
    public void findIowa() {
        assertEquals(State.IA, State.find("IA"), "Short Name");
        assertEquals(State.IA, State.find("Iowa"), "Long Name");
    }

    @Test
    public void findKansas() {
        assertEquals(State.KS, State.find("KS"), "Short Name");
        assertEquals(State.KS, State.find("Kansas"), "Long Name");
        assertEquals(State.KS, State.find("Kan"), "Other Name");
        assertEquals(State.KS, State.find("Kan."), "AP Name");
    }

    @Test
    public void findKentucky() {
        assertEquals(State.KY, State.find("KY"), "Short Name");
        assertEquals(State.KY, State.find("Kentucky"), "Long Name");
        assertEquals(State.KY, State.find("Ky."), "AP Name");
    }

    @Test
    public void findLouisiana() {
        assertEquals(State.LA, State.find("LA"), "Short Name");
        assertEquals(State.LA, State.find("Louisiana"), "Long Name");
        assertEquals(State.LA, State.find("La."), "AP Name");
    }

    @Test
    public void findMaine() {
        assertEquals(State.ME, State.find("ME"), "Short Name");
        assertEquals(State.ME, State.find("Maine"), "Long Name");
    }

    @Test
    public void findMaryland() {
        assertEquals(State.MD, State.find("MD"), "Short Name");
        assertEquals(State.MD, State.find("Maryland"), "Long Name");
        assertEquals(State.MD, State.find("Md."), "AP Name");
    }

    @Test
    public void findMassachusetts() {
        assertEquals(State.MA, State.find("MA"), "Short Name");
        assertEquals(State.MA, State.find("Massachusetts"), "Long Name");
        assertEquals(State.MA, State.find("Mass"), "Other Name");
        assertEquals(State.MA, State.find("Mass."), "AP Name");
    }

    @Test
    public void findMichigan() {
        assertEquals(State.MI, State.find("MI"), "Short Name");
        assertEquals(State.MI, State.find("Michigan"), "Long Name");
        assertEquals(State.MI, State.find("Mich"), "Other Name");
        assertEquals(State.MI, State.find("Mich."), "AP Name");
    }

    @Test
    public void findMinnesota() {
        assertEquals(State.MN, State.find("MN"), "Short Name");
        assertEquals(State.MN, State.find("Minnesota"), "Long Name");
        assertEquals(State.MN, State.find("Minn"), "Other Name - Minn");
        assertEquals(State.MN, State.find("Min"), "Other Name - Min");
        assertEquals(State.MN, State.find("Minn."), "AP Name");
    }

    @Test
    public void findMississippi() {
        assertEquals(State.MS, State.find("MS"), "Short Name");
        assertEquals(State.MS, State.find("Mississippi"), "Long Name");
        assertEquals(State.MS, State.find("Miss"), "Other Name");
        assertEquals(State.MS, State.find("Miss."), "AP Name");
    }

    @Test
    public void findMissouri() {
        assertEquals(State.MO, State.find("MO"), "Short Name");
        assertEquals(State.MO, State.find("Missouri"), "Long Name");
        assertEquals(State.MO, State.find("Mo."), "AP Name");
    }

    @Test
    public void findMontana() {
        assertEquals(State.MT, State.find("MT"), "Short Name");
        assertEquals(State.MT, State.find("Montana"), "Long Name");
        assertEquals(State.MT, State.find("Mont"), "Other Name");
        assertEquals(State.MT, State.find("Mont."), "AP Name");
    }

    @Test
    public void findNebraska() {
        assertEquals(State.NE, State.find("NE"), "Short Name");
        assertEquals(State.NE, State.find("Nebraska"), "Long Name");
        assertEquals(State.NE, State.find("Neb"), "Other Name");
        assertEquals(State.NE, State.find("Neb."), "AP Name");
    }

    @Test
    public void findNevada() {
        assertEquals(State.NV, State.find("NV"), "Short Name");
        assertEquals(State.NV, State.find("Nevada"), "Long Name");
        assertEquals(State.NV, State.find("Nev"), "Other Name");
        assertEquals(State.NV, State.find("Nev."), "AP Name");
    }

    @Test
    public void findNewHampshire() {
        assertEquals(State.NH, State.find("NH"), "Short Name");
        assertEquals(State.NH, State.find("New Hampshire"), "Long Name");
        assertEquals(State.NH, State.find("N.H."), "AP Name");
    }

    @Test
    public void findNewJersey() {
        assertEquals(State.NJ, State.find("NJ"), "Short Name");
        assertEquals(State.NJ, State.find("New Jersey"), "Long Name");
        assertEquals(State.NJ, State.find("N.J."), "AP Name");
    }

    @Test
    public void findNewMexico() {
        assertEquals(State.NM, State.find("NM"), "Short Name");
        assertEquals(State.NM, State.find("New Mexico"), "Long Name");
        assertEquals(State.NM, State.find("N.M."), "AP Name");
    }

    @Test
    public void findNewYork() {
        assertEquals(State.NY, State.find("NY"), "Short Name");
        assertEquals(State.NY, State.find("New York"), "Long Name");
        assertEquals(State.NY, State.find("N.Y."), "AP Name");
    }

    @Test
    public void findNorthCarolina() {
        assertEquals(State.NC, State.find("NC"), "Short Name");
        assertEquals(State.NC, State.find("North Carolina"), "Long Name");
        assertEquals(State.NC, State.find("N.C."), "AP Name");
    }

    @Test
    public void findANorthDakota() {
        assertEquals(State.ND, State.find("ND"), "Short Name");
        assertEquals(State.ND, State.find("North Dakota"), "Long Name");
        assertEquals(State.ND, State.find("N.D."), "AP Name");
    }

    @Test
    public void findOhio() {
        assertEquals(State.OH, State.find("OH"), "Short Name");
        assertEquals(State.OH, State.find("Ohio"), "Long Name");
    }

    @Test
    public void findOklahoma() {
        assertEquals(State.OK, State.find("OK"), "Short Name");
        assertEquals(State.OK, State.find("Oklahoma"), "Long Name");
        assertEquals(State.OK, State.find("Okla"), "Other Name");
        assertEquals(State.OK, State.find("Okla."), "AP Name");
    }

    @Test
    public void findOregon() {
        assertEquals(State.OR, State.find("OR"), "Short Name");
        assertEquals(State.OR, State.find("Oregon"), "Long Name");
        assertEquals(State.OR, State.find("Ore"), "Other Name");
        assertEquals(State.OR, State.find("Ore."), "AP Name");
    }

    @Test
    public void findPennsylvania() {
        assertEquals(State.PA, State.find("PA"), "Short Name");
        assertEquals(State.PA, State.find("Pennsylvania"), "Long Name");
        assertEquals(State.PA, State.find("Penn"), "Other Name");
        assertEquals(State.PA, State.find("Pa."), "AP Name");
    }

    @Test
    public void findPuertoRico() {
        assertEquals(State.PR, State.find("PR"), "Short Name");
        assertEquals(State.PR, State.find("Puerto Rico"), "Long Name");
        assertEquals(State.PR, State.find("P.R."), "AP Name");
    }

    @Test
    public void findRhodeIsland() {
        assertEquals(State.RI, State.find("RI"), "Short Name");
        assertEquals(State.RI, State.find("Rhode Island"), "Long Name");
        assertEquals(State.RI, State.find("R.I."), "AP Name");
    }

    @Test
    public void findSouthCarolina() {
        assertEquals(State.SC, State.find("SC"), "Short Name");
        assertEquals(State.SC, State.find("South Carolina"), "Long Name");
        assertEquals(State.SC, State.find("S.C."), "AP Name");
    }

    @Test
    public void findSouthDakota() {
        assertEquals(State.SD, State.find("SD"), "Short Name");
        assertEquals(State.SD, State.find("South Dakota"), "Long Name");
        assertEquals(State.SD, State.find("S.D."), "AP Name");
    }

    @Test
    public void findTennessee() {
        assertEquals(State.TN, State.find("TN"), "Short Name");
        assertEquals(State.TN, State.find("Tennessee"), "Long Name");
        assertEquals(State.TN, State.find("Tenn"), "Other Name");
        assertEquals(State.TN, State.find("Tenn."), "AP Name");
    }

    @Test
    public void findTexas() {
        assertEquals(State.TX, State.find("TX"), "Short Name");
        assertEquals(State.TX, State.find("Texas"), "Long Name");
        assertEquals(State.TX, State.find("Tex"), "Other Name - Tex");
        assertEquals(State.TX, State.find("Tex."), "Other Name - Tex.");
    }

    @Test
    public void findUtah() {
        assertEquals(State.UT, State.find("UT"), "Short Name");
        assertEquals(State.UT, State.find("Utah"), "Long Name");
    }

    @Test
    public void findVermont() {
        assertEquals(State.VT, State.find("VT"), "Short Name");
        assertEquals(State.VT, State.find("Vermont"), "Long Name");
        assertEquals(State.VT, State.find("Vt."), "AP Name");
    }

    @Test
    public void findVirginia() {
        assertEquals(State.VA, State.find("VA"), "Short Name");
        assertEquals(State.VA, State.find("Virginia"), "Long Name");
        assertEquals(State.VA, State.find("Va."), "AP Name");
    }

    @Test
    public void findWashington() {
        assertEquals(State.WA, State.find("WA"), "Short Name");
        assertEquals(State.WA, State.find("Washington"), "Long Name");
        assertEquals(State.WA, State.find("Wash"), "Other Name");
        assertEquals(State.WA, State.find("Wash."), "AP Name");
    }

    @Test
    public void findWestVirginia() {
        assertEquals(State.WV, State.find("WV"), "Short Name");
        assertEquals(State.WV, State.find("West Virginia"), "Long Name");
        assertEquals(State.WV, State.find("WVa"), "Other Name");
        assertEquals(State.WV, State.find("W.Va."), "AP Name");
    }

    @Test
    public void findWisconsin() {
        assertEquals(State.WI, State.find("WI"), "Short Name");
        assertEquals(State.WI, State.find("Wisconsin"), "Long Name");
        assertEquals(State.WI, State.find("Wis"), "Other Name - Wis");
        assertEquals(State.WI, State.find("Wis."), "AP Name - Wis.");
        assertEquals(State.WI, State.find("Wisc"), "Other Name - Wisc");
        assertEquals(State.WI, State.find("Wisc."), "AP Name - Wisc.");
    }

    @Test
    public void findWyoming() {
        assertEquals(State.WY, State.find("WY"), "Short Name");
        assertEquals(State.WY, State.find("Wyoming"), "Long Name");
        assertEquals(State.WY, State.find("Wyo"), "Other Name");
        assertEquals(State.WY, State.find("Wyo."), "AP Name");
    }
}
