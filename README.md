# Routines


## Purpose 
This is a habit tracking app to make sure you can stay on top of the habits you care about most. You can create a specif habit and choose the days you would like it to occur on. Becuase of the time constraint we chose to allow a user to create a single habit event *PER DAY* of occurance to denote if the habit has been completed on time. The app allows the user to upload a photo and by default will include the current location of where the habit event occured. 
* Ex. If you wanted to play basketball on Wednesdays and Fridays, the app would keep track of every Wednesday and Friday since its creation and keep track of how many Wednesdays and Fridays you played basketball on. 
The app 
## To run the app:
We have the Pixel 2 Emulator (with google services plugin), it is recommended when using / testing the app you also use the same emulator it was developed on.

## Issues with Intent Testing 
Robotium Documentations says it is not capable of testing anything outside of the app, and after spending countless hours trying to get it to work we have failed. The two intent tests affected are the ones that allows the user to take a photo from the camera or choose a photo from the gallery. These were not able to be properly intent tested using Robotium. Because we used Robotium throughout the progress we did not have enough time to learn another testing Framework. Below is a link to the Robotium Documentation. 
* See: https://github.com/RobotiumTech/robotium/wiki/Questions-&-Answers
