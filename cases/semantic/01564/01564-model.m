(*

category:        Test
synopsis:        Uncommon MathML in kinetic laws.
componentTags:   Compartment, Reaction, Species
testTags:        Amount, UncommonMathML
testType:        TimeCourse
levels:          2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

The model tests various uncommon mathematical constructs in kinetic laws.

The model contains:
* 52 species (S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, S12, S13, S14, S15, S16, S17, S18, S19, S20, S21, S22, S23, S24, S25, S26, S27, S28, S29, S30, S31, S32, S33, S34, S35, S36, S37, S38, S39, S40, S41, S42, S43, S44, S45, S46, S47, S48, S49, S50, S51)
* 1 compartment (C)

There are 52 reactions:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: -> S0 | $exponentiale$ |
| J1: -> S1 | $exp(exponentiale)$ |
| J2: -> S2 | $abs(-1)$ |
| J3: -> S3 | $abs(1)$ |
| J4: -> S4 | $acos(-1)$ |
| J5: -> S5 | $acos(0.5)$ |
| J6: -> S6 | $asin(1)$ |
| J7: -> S7 | $asin(-0.5)$ |
| J8: -> S8 | $atan(2.8)$ |
| J9: -> S9 | $atan(-7.09)$ |
| J10: -> S10 | $ceil(0.5)$ |
| J11: -> S11 | $ceil(3.55)$ |
| J12: -> S12 | $ceil(-4.6)$ |
| J13: -> S13 | $cos(9.1)$ |
| J14: -> S14 | $cos(-0.22)$ |
| J15: -> S15 | $exp(0)$ |
| J16: -> S16 | $exp(1)$ |
| J17: -> S17 | $exp(0.77)$ |
| J18: -> S18 | $floor(-4.6)$ |
| J19: -> S19 | $floor(9.1)$ |
| J20: -> S20 | $ln(0.2)$ |
| J21: -> S21 | $ln(1)$ |
| J22: -> S22 | $log10(0.2)$ |
| J23: -> S23 | $log10(1)$ |
| J24: -> S24 | $1^2$ |
| J25: -> S25 | $2^2$ |
| J26: -> S26 | $1.4^5.1$ |
| J27: -> S27 | $4^2$ |
| J28: -> S28 | $3.1^2$ |
| J29: -> S29 | $sqrt(4)$ |
| J30: -> S30 | $sqrt(7.4)$ |
| J31: -> S31 | $sin(2.1)$ |
| J32: -> S32 | $sin(0)$ |
| J33: -> S33 | $sin(-5.9)$ |
| J34: -> S34 | $tan(0)$ |
| J35: -> S35 | $tan(1.11)$ |
| J36: -> S36 | $tan(-6)$ |
| J37: -> S37 | $sec(0.5)$ |
| J38: -> S38 | $csc(4.5)$ |
| J39: -> S39 | $cot(0.2)$ |
| J40: -> S40 | $sinh(0.3)$ |
| J41: -> S41 | $cosh(1.7)$ |
| J42: -> S42 | $arcsec(2.3)$ |
| J43: -> S43 | $arccsc(1.1)$ |
| J44: -> S44 | $arccot(-0.1)$ |
| J45: -> S45 | $arcsinh(99)$ |
| J46: -> S46 | $arccosh(1.34)$ |
| J47: -> S47 | $arctanh(-0.7)$ |
| J48: -> S48 | $arcsech(0.42)$ |
| J49: -> S49 | $arccsch(0.01)$ |
| J50: -> S50 | $times()$ |
| J51: -> S51 | $plus()$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial concentration of species S0 | $0$ | variable |
| Initial concentration of species S1 | $0$ | variable |
| Initial concentration of species S2 | $0$ | variable |
| Initial concentration of species S3 | $0$ | variable |
| Initial concentration of species S4 | $0$ | variable |
| Initial concentration of species S5 | $0$ | variable |
| Initial concentration of species S6 | $0$ | variable |
| Initial concentration of species S7 | $0$ | variable |
| Initial concentration of species S8 | $0$ | variable |
| Initial concentration of species S9 | $0$ | variable |
| Initial concentration of species S10 | $0$ | variable |
| Initial concentration of species S11 | $0$ | variable |
| Initial concentration of species S12 | $0$ | variable |
| Initial concentration of species S13 | $0$ | variable |
| Initial concentration of species S14 | $0$ | variable |
| Initial concentration of species S15 | $0$ | variable |
| Initial concentration of species S16 | $0$ | variable |
| Initial concentration of species S17 | $0$ | variable |
| Initial concentration of species S18 | $0$ | variable |
| Initial concentration of species S19 | $0$ | variable |
| Initial concentration of species S20 | $0$ | variable |
| Initial concentration of species S21 | $0$ | variable |
| Initial concentration of species S22 | $0$ | variable |
| Initial concentration of species S23 | $0$ | variable |
| Initial concentration of species S24 | $0$ | variable |
| Initial concentration of species S25 | $0$ | variable |
| Initial concentration of species S26 | $0$ | variable |
| Initial concentration of species S27 | $0$ | variable |
| Initial concentration of species S28 | $0$ | variable |
| Initial concentration of species S29 | $0$ | variable |
| Initial concentration of species S30 | $0$ | variable |
| Initial concentration of species S31 | $0$ | variable |
| Initial concentration of species S32 | $0$ | variable |
| Initial concentration of species S33 | $0$ | variable |
| Initial concentration of species S34 | $0$ | variable |
| Initial concentration of species S35 | $0$ | variable |
| Initial concentration of species S36 | $0$ | variable |
| Initial concentration of species S37 | $0$ | variable |
| Initial concentration of species S38 | $0$ | variable |
| Initial concentration of species S39 | $0$ | variable |
| Initial concentration of species S40 | $0$ | variable |
| Initial concentration of species S41 | $0$ | variable |
| Initial concentration of species S42 | $0$ | variable |
| Initial concentration of species S43 | $0$ | variable |
| Initial concentration of species S44 | $0$ | variable |
| Initial concentration of species S45 | $0$ | variable |
| Initial concentration of species S46 | $0$ | variable |
| Initial concentration of species S47 | $0$ | variable |
| Initial concentration of species S48 | $0$ | variable |
| Initial concentration of species S49 | $0$ | variable |
| Initial concentration of species S50 | $0$ | variable |
| Initial concentration of species S51 | $0$ | variable |
| Initial volume of compartment 'C' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
