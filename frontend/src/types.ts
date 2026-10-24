export type DataCenter = {
  nome: string;
  bastidores: Bastidor[];
};

export type Bastidor = {
  nome: string;
  nickname?: string | null;
  equipamentos: Equipamento[];
};

export type Equipamento = {
  name: string;
  cor?: string | null;
};
