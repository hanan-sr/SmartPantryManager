\# Database Design



The application uses SQLite for local data storage. The database is named

smart\_pantry.db and is managed through the DatabaseHelper class, which

extends SQLiteOpenHelper.



\## Database Tables



\### 1. pantry

Stores ingredients belonging to the user's pantry.



| Column | Data type | Description |

|---|---|---|

| id | INTEGER | Primary key |

| name | TEXT | Ingredient name (unique) |

| quantity | REAL | Available quantity |

| unit | TEXT | Measurement unit |

| expiry\_date | TEXT | Optional expiry date |



\### 2. recipes

Stores the available recipes.



| Column | Data type | Description |

|---|---|---|

| id | INTEGER | Primary key |

| name | TEXT | Recipe name |

| method | TEXT | Preparation instructions |



\### 3. recipe\_ingredients

Stores the ingredients required by each recipe.



| Column | Data type | Description |

|---|---|---|

| id | INTEGER | Primary key |

| recipe\_id | INTEGER | Foreign key referencing recipes.id |

| name | TEXT | Required ingredient |

| quantity | REAL | Required quantity |

| unit | TEXT | Measurement unit |



\## Entity Relationships



A recipe can contain multiple recipe ingredients. The recipe\_id

foreign key connects recipe\_ingredients to recipes.



The pantry table independently stores the ingredients available

to the user.



```mermaid

erDiagram

&#x20;   RECIPES ||--o{ RECIPE\_INGREDIENTS : contains



&#x20;   RECIPES {

&#x20;       INTEGER id PK

&#x20;       TEXT name

&#x20;       TEXT method

&#x20;   }



&#x20;   RECIPE\_INGREDIENTS {

&#x20;       INTEGER id PK

&#x20;       INTEGER recipe\_id FK

&#x20;       TEXT name

&#x20;       REAL quantity

&#x20;       TEXT unit

&#x20;   }



&#x20;   PANTRY {

&#x20;       INTEGER id PK

&#x20;       TEXT name

&#x20;       REAL quantity

&#x20;       TEXT unit

&#x20;       TEXT expiry\_date

&#x20;   }

