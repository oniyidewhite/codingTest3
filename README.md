# MobilePay
**Coding TasK**                                                                                                    
>- We want you to create a single activity Android application    
>- Input would be a search term for places used for performing or producing music  
>- Output would be places as pins on a map   

**Requirements**
>- Must be written in Kotlin   
>- Use MusicBrainz API https://wiki.musicbrainz.org/Development
>- Places returned per request should be limited, but all places must be displayed on map. For example there 100 places for search term, but limit is 20, so you need 5 request to get all the places 
>- Make this limit easy to tune in code    
>- Displayed places should be open from 1990  
>- Every pin has a lifespan, meaning after it expires, pin should be removed from the map. 
>- Lifespan calculation: open_year - 1990 = lifespan_in_seconds. Example: 2017 - 1990 = 27s   

**Libraries**
>Below are the libraries I used for the project.
>- Hilt: For my Dependency Injection, so I can focus more on the task
>- Mavericks: Android MVI framework, Helps abstract my UI, activity, viewModel.
>- Epoxy: Allows me to build complex ui easily on recyclerView
>- Navigation: Android arch components for navigation
>- Mockito: Unit testing library
>- Googlemap: To display places on the map 

**Other Information**
>- API documentation: https://wiki.musicbrainz.org/MusicBrainz_API/Search


**Approach**
>- Overall, I broke down the problems to small pieces called `State`. State simply represents the brain of a screen. `Event` are anything that just happened that State should know about.
>- I configured github actions to run both lint and test


**Assumptions**
>- I assumed the user's network would be fine to an extent as i implemented a more subtle error handling + retry
