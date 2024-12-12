Let's build a hit counter using a [functional domain model](https://pragprog.com/titles/swdddf/domain-modeling-made-functional/) based on the [Tagless-Final](https://blog.rockthejvm.com/tagless-final/) style and [The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

This project is based on the post [F[unctional] Core, IO[mperative] Shell](https://earldouglas.com/clean.html).

## Layers

The domain is the lowest layer. Each additional layer may access any of the layers beneath it.

```
.-------------------------------------------.
| LAYER 4: RUNNERS                        o |
|-----------------------------------------|-|
| * Main method with an http server       | |
'-----------------------------------------|-'
                                          |
.-----------------------------------------|-.
| LAYER 3: DRIVERS                     o  v |
|--------------------------------------|--|-|
| * In-memory data store               |  | |
| * RDBMS/JDBC data store              |  | |
'--------------------------------------|--|-'
                                       |  |
impure                                 |  |
=======================================|==|==
pure                                   |  |
                                       |  |
.--------------------------------------|--|-.
| LAYER 2: USE CASES                o  v  v |
|-----------------------------------|--|--|-|
| * Increment and get the count     |  |  | |
'-----------------------------------|--|--|-'
                                    |  |  |
.-----------------------------------|--|--|-.
| LAYER 1: DOMAIN                   v  v  v |
|-------------------------------------------|
| * Entities                                |
|     * Count                               |
| * Operations                              |
|     * Increment the count                 |
|     * Get the count                       |
'-------------------------------------------'
```
