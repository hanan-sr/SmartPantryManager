\# Functional Testing



Testing was performed manually using the Android Studio Medium

Phone emulator.



| Test | Expected result | Result |

|---|---|---|

| Add ingredient | Ingredient appears in pantry | Passed |

| Edit ingredient | Updated quantity is displayed | Passed |

| Delete ingredient | Ingredient disappears | Passed |

| Save empty name | Application rejects input | Passed |

| Enter negative quantity | Application rejects input | Passed |

| Match complete recipe | Recipe appears in suggestions | Passed |

| Reduce required quantity | Recipe disappears | Passed |

| Restart application | Pantry data remains saved | Passed |

| Open recipe details | Ingredients and method appear | Passed |

| Delete all ingredients | Empty-state message appears | Passed |

| Open Settings | Settings screen works | Passed |



\## Recipe Matching Test



The following ingredients were entered:



\- Tomato: 2 pcs

\- Egg: 2 pcs

\- Salt: 2 g



Tomato and Egg Scramble appeared in the suggestions.



The tomato quantity was then reduced to 1 pcs. The recipe

disappeared because the required quantity was no longer available.



This confirmed that the application uses strict recipe matching.

