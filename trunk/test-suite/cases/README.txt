The SBML Test Suite comes in three sections:

Semantic:
        * The semantic test suite contains valid SBML models with known
          simulation results.  They are used to test simulator accuracy.
          Each test consists of a directory containing the model (or models,
          if that model can be translated to other SBML levels/versions
          without semantic loss), instructions on how to simulate that model,
          and the expected results.  Broad categories of what is being tested
          are included as 'tags'.

Stochastic:
        * The stochastic test suite contains valid SBML models with known
          stochastic simulation results.  They are used to test stochastic
          simulator accuracy. Each test consists of a directory containing the
          model (or models, if that model can be translated to other SBML
          levels/versions without semantic loss), instructions on how to
          simulate that model, and the expected results.  Because the expected
          results are stochastic, each test is designed to be run multiple
          times, with summary statistics collected for each, which are then
          compared to the expected summary statistics using a formula derived
          from the number of times the test was repeated.  Broad categories of
          what is being tested are included as 'tags'.

Syntactic:
        * The syntactic test suite consists of valid and invalid SBML models.
          Each test is designed to check a particular validation rule, and
          each model in the test directory will either fail or pass that
          validation rule, according to its filename.  Details about the
          error and the error message produced by libsbml are included, as are
          'incidental' warnings that the model may also produce.
