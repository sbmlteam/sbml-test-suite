(* 

category:      Test
synopsis:      A three-step reaction scheme with a species conversion factor.
componentTags: Parameter, Species, Reaction
testTags:      InitialAmount, InitialValue, ConversionFactors
testType:      TimeCourse
levels:        3.1
generatedBy:   Numeric

 This model contains three reactions:
[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| -> S1| k1|
| 2S1 -> 3S2| k2 * S1 / S2|
| S2 -> | k3 * S2|]

 In addition, there is a conversion factor ('S1conv') on species S1.  This means that S1 is converted according to S1conv, and S2 is not converted.  This changes how S1 change in time, but not how S1 is interpreted in the reaction rates.

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of S1        |$0$                  |
|Initial amount of S2        |$0.001$              |
|Value of parameter k1       |$1$          |
|Value of parameter k2       |$3$          |
|Value of parameter k3       |$1.4$          |
|Value of parameter S1conv       |$3$          |]

*)
