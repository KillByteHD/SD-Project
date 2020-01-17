# SD-Project

## Description
Distributed Systems project instance

### Funcionalidades Adicionais

#### Limite de descargas:

- [x] O servidor deve garantir que em cada momento não há mais do que MAXDOWN descargas de ficheiros a ocorrer em simultâneo.

- [x] Pedidos de descarga que ultrapassem o limite devem esperar até obterem vez.

- [x] Em caso de espera, deve ser estabelecida uma política que combine a ordem de chegada com a
necessidade de todos os utilizadores obterem ficheiros.

#### Notificação de novas músicas:

- [x] Durante o período em que um cliente está ligado ao servidor, deve receber notificações de novas músicas que sejam carregadas.

- [x] Cada notificação deve conter o título e autor da música.

- [x] Deve ser possível continuar a receber e mostrar notificações mesmo durante outras operações
demoradas de carga ou descarga de ficheiros.

#### Tamanho dos ficheiros ilimitado:

- [x] A solução apresentada não deve assumir que cada ficheiro de música cabe completamente em
memória.

- [x] Pelo contrário, deve garantir que a transferência de cada ficheiro nunca ocupa mais do que MAX_SIZE bytes em memória, tanto do cliente como do servidor

### TODO

- Meter C2DRequest abstract

- Criar interfaces para todos os tipos de Requests e Replys

### NOTAS

Em Music metodos que apenas lêem atributos dessa class nao precisam de lock pois os campos sao 
construidos 1 vez apenas e nao podem ser alterados , nao havendo necessidade de synchronized em
metodos como por exemplo `authorAndName()` ou getters


Quando estao a ser dadas tags ao upload não se pode dar a tag "(none)" por causa da estrutura do protocologo


Formatar melhor a apresentação do search