# maestro
Orchestrate your microservices



	███╗░░░███╗░█████╗░███████╗░██████╗████████╗██████╗░░█████╗░
	████╗░████║██╔══██╗██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗
	██╔████╔██║███████║█████╗░░╚█████╗░░░░██║░░░██████╔╝██║░░██║
	██║╚██╔╝██║██╔══██║██╔══╝░░░╚═══██╗░░░██║░░░██╔══██╗██║░░██║
	██║░╚═╝░██║██║░░██║███████╗██████╔╝░░░██║░░░██║░░██║╚█████╔╝
	╚═╝░░░░░╚═╝╚═╝░░╚═╝╚══════╝╚═════╝░░░░╚═╝░░░╚═╝░░╚═╝░╚════╝░

Tada !

**Sample Configuration File:**
````
{
    "servers": [
        {
            "name": "Server 1",
            "ip": "http://192.168.1.100:9090",
            "applications": [
                {
                    "name": "app1",
                    "serviceName": "mydemo",
                    "logs": [
                        "/projects/demo/logs/"
                    ]
                }
            ]
        },
        {
            "name": "Server 2",
            "ip": "http://192.168.1.200:9080",
            "applications": [
                {
                    "name": "app2",
                    "serviceName": "mydemo1",
                    "logs": [
                        "/projects/demo1/logs/"
                    ]
                }
            ]
        }
    ]
}
````
