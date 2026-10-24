# RackView

Aplicação para gestão de equipamentos em data centers e bastidores.

## Como funciona

Os `DataCenter` e os `Bastidor` são criados automaticamente pela aplicação a partir da configuração da aplicação. Isto significa que o utilizador não pode alterar o nome dos data centers nem a estrutura dos bastidores.

O utilizador é responsável por criar e gerir os equipamentos dentro dos bastidores existentes.

### Configuração inicial

No `application.properties`, a aplicação já define os data centers e bastidores iniciais:

```properties
rackview.data-centers[0].nome=DC-01
rackview.data-centers[0].bastidores[0].nome=Rack-A
rackview.data-centers[0].bastidores[1].nome=Rack-B

rackview.data-centers[1].nome=DC-02
rackview.data-centers[1].bastidores[0].nome=Rack-C
rackview.data-centers[1].bastidores[1].nome=Rack-D
```

### Regra de negócio

- Data centers: pré-definidos pela aplicação
- Bastidores: pré-definidos pela aplicação
- Equipamentos: criados pelo utilizador dentro dos bastidores

Assim, a estrutura física da infraestrutura fica fixa e o utilizador apenas adiciona os equipamentos necessários ao bastidor correspondente.
