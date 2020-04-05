
## The Project

The project was created as a part of a test task for JetBrains Internship 2020 (JetRide. Corporate ridesharing)

It contains a Gradle Java (Kotlin) app, so it must be run (or built) appropriately.

## Idea of the best suggestion.

First of all, we know that me must recommend for each passenger independently, so WLOG we assume there's only one passenger exists.

The thing happened to drivers those didn't take the passenger is they reached their destination with no loss in time
(notice here time equals distance, because all speeds are the same). If a driver is considered as a candidate to driver the passenger,
their route differs.

We could measure the loss taken by a driver, but we don't know the starting point (so it's impossible to solve
the triangle). So we compare drivers the other way: we just measure how long (= far) must the driver wait until reaching their endpoint.
It transforms to "How far is the passenger's point from the driver's one?". So we order according to answers to that question.
The solution is not the best, because, first, we ignore other passengers (imp. not to ignore), second, we ignore the real differ in route
of drivers (imp. not to ignore as well).
