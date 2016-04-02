# FeatureSelection

This project includes our algorithm IVEA-II used to select features in software product lines and some feature models.

IVEA-II algorithm
-----------------
This is a multi-objective optimization algorithm which aims to select features in software product lines (SPLs) in the face of multiple objectives which are higily likey to be competing and conflicting. At the same time the features have massive dependencies and constraints among themselves. In practice, these relationships are mandotory rules that have to be conformed by all of the derived products in the SPL. 
There are three primary features in IVEA-II.
1. We design a two-dimensional fitness function to control the feature relationships conformance and optimize the multiple objectives meanwhile.
1. We introduce crownding distance to improve the diversity of the approximate solutions.
1. We design a new mutation operator to enhance the number of valid solutions which keeps all of the relationships among features, and to improve the quality of the final optimal solutions.

Feature models
--------------
This file includes 6 models: ecos(3.0),fiasco(2011081207), freebsd(8.0.0), uClinux(20100825), Linux (2.6.28.6) and webProtal. The first five models are large-scale from (http://code.google.com/p/linux-variability-analysis-tools). And the last small model is selected from an online feature model repository SPLOT (http://www.splot-research.org/). 

For each model, there are five files: features and their relationships in .dimacs, three generated attribute values on each feature (.augment),core features (.mandatory), dead features (.dead), 31 valid solutions with one feature-rich and 30 randomly selected (.richseed).
