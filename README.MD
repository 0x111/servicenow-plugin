## servicenow-plugin

The plugin exists to simplify the developing process while you edit Servicenow scripts.
With this plugin, you will be able to download, edit and upload the script back to your instance and then test it afterwards.
The plugin is avaliable in the Jetbrains plugin repository too for simple installation [ServiceNow Plugin]()

### Installation

The Installation is simple. You can install the plugin in any editor of your choice from the Jetbrains family.
Although I think WebStorm is the best for editing javascript, but the plugin should work with IntelliJ and others too.
After the installation, you will see the new options in the `Tools` menu of your IDE.

<img src="https://p1.picsto.re/W0Png.png" />

As you can see, you have three options. Either to Download or Upload a script or in the end, modify the config file.
If your config file does not exists, then if you initiate a Download or Upload of a script, the plugin will detect it and ask you to fill out the form.

### Config File

The Config file consits of three options for now.
* domain (dev9770870.service-now.com) // Just the plain domain, not an url!!
* username (admin)
* password (your password)

<img src="https://p1.picsto.re/tadlK.png" />

If you fill out the config file with invalid information, the plugin will not work and display some kind of error message to inform you about this problem.

### Usage

#### Download
The usage is simple, like you could see before, there are shortcodes avaliable to the various tasks, so you do not need to open the Tools menu all the time.
If you would like to display the Download form, you use the `Ctrl+Alt+G` shortcut and the Download form will be displayed right away.

<img src="https://p1.picsto.re/JKACx.png" />

There is an example for filling out the input field, but to be sure.

The first value is the tablename, which is sys_script (a.k.a. system script, a.k.a. business rule).

The second value is the field name, which is in most of the cases `script`.

The third value is the sys_id, which is the unique identifier for the given script.
These values are separated with the `:` delimiter.
If you fill these out correctly, you should be able to download the script successfully.

#### Upload

After you edit your previously downloaded script, you should be ready to upload it, to do so, you can use the `Ctrl+Alt+U` shortcut.
If you have your script open in the web editor of ServiceNow, then you should be able to see the changes, after the script is uploaded.

### Notes

I plan to do some more improvements, like code autocomplete and others. I am still learning about this system of plugin development.
If there is something to be improved (which I am sure there is), then please make a pull request or raise an issue.
If you have new features suggestions then please create an issue for that.

The project is licensed under the MIT License.

### Dependencies
The plugin has various dependencies which I will include in the lib folder of this project.
After you clone the repository, you need to add them manually into your sources.

