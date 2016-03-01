To get started:

1. Set up the environment:
        * Download weather_envrc from the Google Drive folder and save it as .envrc
        * Launch IntelliJ from within the repo root: `idea .`
2. Import the gradle file:
        * Import Project from the intellij splash screen
        * Select build.gradle from the repo root
        * Accept the defaults
3. Fix the project's Java configuration:
        * File -> Project Structure
        * Select Project Settings/Project on the left
        * Change the project language level to at least 8.
4. Tell IntelliJ how to run it:
        * Right click and run application class
        * Run -> Edit Configurations, Select application class
        * Remove intellij 'make' command
        * Add gradle task 'assemble'
5. Tell compiler that lombok is a thing
        * Preferences -> Plugins -> Install
        * Install lombok plugin
