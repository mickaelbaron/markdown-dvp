Un outil qui transforme un document au format Markdown vers le format XML de Developpez.com. Le format Markdown pris en compte est défini ici : https://daringfireball.net/projects/markdown/syntax. Des fonctionnalités étendues sont également prises en comptes.

# Compilation

Pour générer le binaire _markdowntodvp.jar_, exécuter l'instruction suivante

    $ mvn clean package

# Exécution sans en-tête et bas de page

Supposons que votre document Markdown soit stocké dans le fichier _exemple.md_, exécuter la commande suivante pour générer un document conforme au schéma XML Developpez.com.

    $ java -jar markdowntodvp.jar exemple.md --format=dvp --useextension=true

Le résultat sera généré sur la sortie standard. Faites une redirection pour générer le résultat dans un fichier.

    $ java -jar markdowntodvp.jar exemple.md --format=dvp --useextension=true > exemple.xml

# Exécution avec en-tête et bas de page

Il est possible d'obtenir un document XML Developpez.com complet à partir d'un fichier en-tête et de bas de page. Ce fichier en-tête et base de page doit contenir trois parties comme présentées ci-dessous

	<?xml version="1.0" encoding="ISO-8859-1"?>
	<document>
		<summary>
		<!-- ### -->
		</summary>
	</document>

Le document généré sera placé au niveau de `<!-- ### -->`

Pour exécuter avec un fichier en-tête et bas de page _headerfootersample.txt_

	$ java -jar markdowntodvp.jar exemple.md --format=dvp --useextension=true --headerfooterfile=headerfootersample.txt

# Paramètres d'exécution

* --format : sélectionne le format de sortie. La valeur pour Developpez.com est dvp (ex : `--format=dvp`)
* --useextension : active la gestion des fonctionnalités étendues de Markdown (ex : `--useextension=true`)
* --headerfooterfile : spécifie le fichier d'en-tête et bas de page (ex : `--headerfooterfile=headerfootersample.txt`)
