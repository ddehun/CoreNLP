package edu.stanford.nlp.math;

import edu.stanford.nlp.semgraph.semgrex.ssurgeon.AddDep;
import edu.stanford.nlp.util.Triple;
import junit.framework.TestCase;

public class SloppyMathTest extends TestCase {

  @Override
  public void setUp() {
  }

  //ChaeHun
  public void testbasicMethods(){
    DoubleAD a = new DoubleAD(1,0.1);
    DoubleAD b = new DoubleAD(2,0.0);
    DoubleAD ab = new DoubleAD(2,0.2);
    DoubleAD a_plus_b = new DoubleAD(3,0.1);
    DoubleAD a_div_const_b = new DoubleAD(0.5,0.05);
    DoubleAD a_minus_b = new DoubleAD(-1,0.1);
    assertEquals(ab, ADMath.mult(a,b));
    assertEquals(ab, ADMath.multConst(a,2));
    assertEquals(a_plus_b,ADMath.plus(a,b));
    assertEquals(a_div_const_b,ADMath.divideConst(a,2));
    assertEquals(a_minus_b, ADMath.minus(a,b));

    DoubleAD logA = new DoubleAD();
    logA.setval(Math.log(1));
    logA.setdot(a.getdot()/a.getval());
    assertEquals(logA,ADMath.log(a));

    DoubleAD A_plusconst = new DoubleAD();
    A_plusconst.setval(a.getval()+0.1);
    A_plusconst.setdot(a.getdot());
    assertEquals(A_plusconst,ADMath.plusConst(a,0.1));

    DoubleAD A_minusconst = new DoubleAD();
    A_minusconst.setval(a.getval()-0.1);
    A_minusconst.setdot(a.getdot());
    assertEquals(A_minusconst,ADMath.minusConst(a,0.1));
  }

  //ChaeHun
  public void testinvalidHypermethod(){
    try {
      int k = 14;
      int n = 142;
      int r = 3;
      int m = 1242;
      double res = SloppyMath.hypergeometric(k,n,r,m);
      throw new RuntimeException("Should have failed");
    } catch (IllegalArgumentException e) {
    }
  }

  //ChaeHun
  public void testvalidHypermethod(){
      int k = 14;
      int n = 342;
      int r = 112;
      int m = 12;
      double res = SloppyMath.hypergeometric(k,n,r,m);
      assertEquals(0.0,res  );
      k = 7;
      n = 52;
      r = 26;
      m = 10;
      res = SloppyMath.hypergeometric(k,n,r,m);
      assertEquals(0.10810855762394021,res  );
  }

  public void testRound1() {
    assertEquals(0.0, SloppyMath.round(0.499));
    assertEquals(0.0, SloppyMath.round(-0.5));
    assertEquals(10.0, SloppyMath.round(10));
    assertEquals(10.0, SloppyMath.round(10.32));
  }

  public void testRound2() {
    assertEquals(3.14, SloppyMath.round(Math.PI, 2));
    assertEquals(400.0, SloppyMath.round(431.5, -2));
    assertEquals(432.0, SloppyMath.round(431.5, 0));
    assertEquals(0.0, SloppyMath.round(-0.05, 1));
    assertEquals(-0.05, SloppyMath.round(-0.05, 2));
  }

  public void testMax() {
    assertEquals(3,SloppyMath.max(1,2,3));
  }

  public void testMin() {
    assertEquals(1,SloppyMath.min(1,2,3));
  }

  public void testIsDangerous() {
    assertTrue(SloppyMath.isDangerous(Double.POSITIVE_INFINITY) &&
                 SloppyMath.isDangerous(Double.NaN) &&
                 SloppyMath.isDangerous(0));
  }

  public void testIsVeryDangerous() {
    assertTrue(SloppyMath.isDangerous(Double.POSITIVE_INFINITY) &&
                 SloppyMath.isDangerous(Double.NaN));
  }

  public void testLogAdd() {
    double d1 = 0.1;
    double d2 = 0.2;
    double lsum = SloppyMath.logAdd(d1,d2);
    double myLsum = 0;
    myLsum += Math.exp(d1);
    myLsum += Math.exp(d2);
    myLsum = Math.log(myLsum);
    assertTrue(myLsum == lsum);
  }

  public void testIntPow() {
    assertTrue(SloppyMath.intPow(3,5)==Math.pow(3,5));
    assertTrue(SloppyMath.intPow(3.3,5)-Math.pow(3.3,5) < 1e-4);
    assertEquals(1, SloppyMath.intPow(5,0));
    assertEquals(3125, SloppyMath.intPow(5,5));
    assertEquals(32, SloppyMath.intPow(2,5));
    assertEquals(3, SloppyMath.intPow(3,1));
    assertEquals(1158.56201, SloppyMath.intPow(4.1, 5), 1e-4);
    assertEquals(1158.56201f, SloppyMath.intPow(4.1f, 5), 1e-2);
  }

  public void testArccos() {
    assertEquals(Math.PI, SloppyMath.acos(-1.0), 0.001);
    assertEquals(0, SloppyMath.acos(1.0), 0.001);
    assertEquals(Math.PI / 2, SloppyMath.acos(0.0), 0.001);
    for (double x = -1.0; x < 1.0; x += 0.001) {
      assertEquals(Math.acos(x), SloppyMath.acos(x), 0.001);
    }
    try {
      SloppyMath.acos(-1.0000001);
      assertFalse(true);
    } catch (IllegalArgumentException e) { }
    try {
      SloppyMath.acos(1.0000001);
      assertFalse(true);
    } catch (IllegalArgumentException e) { }
  }

  public void testPythonMod() {
    assertEquals(0, SloppyMath.pythonMod(9, 3));
    assertEquals(0, SloppyMath.pythonMod(-9, 3));
    assertEquals(0, SloppyMath.pythonMod(9, -3));
    assertEquals(0, SloppyMath.pythonMod(-9, -3));
    assertEquals(2, SloppyMath.pythonMod(8, 3));
    assertEquals(1, SloppyMath.pythonMod(-8, 3));
    assertEquals(-1, SloppyMath.pythonMod(8, -3));
    assertEquals(-2, SloppyMath.pythonMod(-8, -3));
  }

  public void testParseDouble() {
    for (int base = -10; base < 10; ++base) {
      if (base == 0) { continue; }
      for (int exponent = -100; exponent < 100; ++exponent) {
        double number = Math.pow(Math.PI * base, exponent);
        Triple<Boolean, Long, Integer> parts = SloppyMath.segmentDouble(number);
        double parsed = SloppyMath.parseDouble(parts.first, parts.second, parts.third);
        assertEquals(number, parsed, Math.abs(parsed) / 1.0e5);
      }
    }
  }

  public void testParseInt() {
    assertEquals(42, SloppyMath.parseInt("42"));
    assertEquals(-42, SloppyMath.parseInt("-42"));
    assertEquals(42000000000000l, SloppyMath.parseInt("42000000000000"));
  }
}

