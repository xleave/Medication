
    Voice Reminders

    Use voice reminders to remind users when to take their medicine. 

    Should be able to choose from a selection of different voices. 

    Users should have the option to record their own voice. 

        We will need an API for either Text-to-Speach or playing pre-recorded segments of audio

        "As a user who is blind, I should be able to hear what medications I need to take"

 

    Medication Scheduling 

    Allow users to set up reminders for different medicines at different schedules (i.e.: Take this one 2x daily, Take this one once a day) 

    Overdose Prevention 

    Track dosages taken and alert user if they are nearing their limit for the medication in a given time. 

        To implement the medication scheduling, I'll need an interface to be able to input the name of the medicine, the strength, quantity to take, quantity in each prescription and how often to take.

        We will need a mechanism to count how many medicines have been taken in a specific period of time and send an alert if levels are reaching a limit.

        "As a user with memory problems, I need a safe alerting system to remind me to take my medication and warn me if I am taking too much"

 

    History Tracking 

    Maintain a log of taken medications, missed doses and user adherence.  

        In order to create this feature, we will need a way of assigning medications to a specific user account. We will need to create an account creation and sign-in feature so medicines are unique to user.

        "As a care home owner with a single PC, multiple users should be able to log on the same system with their own account and track their medications"

 

    Notifications and Alerts:  

    Provide visual and auditory notifications, With the ability to snooze or dismiss reminders. 

        We will need to have some sort of way of interfacing with the user either by using Java alert events in JPanels or interfacing with the OS notifications on Windows, Mac, Linux.

        "As someone who uses a PC for other tasks, I need to be notified about medication scheduling even if I am not using the app"

 

    Emergency Contacts: 

    In case of missed doses or overdose risk, alert predefined emergency contacts via email or SMS (requires integration). 

        We will need to include an email API for automated messaging when a dose is missed.

        We will also need an interface where users can set up email addresses for their chosen emergency contacts

        "As someone who cares for an elderly person with dementia, I need to be alerted if their life is in danger of an overdose"

Optional Features:

    Barcode Scanning 
    Allow users to add medications by scanning barcode or prescription label. 

        We will need to use a library for barcode scanning. OpenCV.

        We will need a way to then log the medicines into the users account such as name, how many is in the prescription.

        Need to interface with a camera

        "As someone who cannot type due to hand problems, It would be good to use a camera with the app"

        "As someone who has problems with memory and numbers, I cannot remember long barcode numbers and so using a camera would be easier"

 

    Refill Reminders:  

    Notify users when medication supplies are running low and it’s time to request a refill. 

        We will need to have a mechanism that calculates how many pills are in a prescription, how many to take in a given time and then work out how many pills are remaining.

        Again, will need an alerting system to notify users when they are running low.

        "As someone who is on multiple prescriptions and has a personal carer, I need to be able to calculate how many of each prescription I have left so they can order it for me."

 

    Multilingual Support: 

     Provide voice reminders and interface options in multiple languages. Any other useful feature (Ask Clients) 

        Optionally, should be able to display multiple languages.

        Will need to implement this on all UI

        "As someone who is bi-lingual, I need to be able to use both languages.
