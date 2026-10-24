const API_BASE_URL = 'http://localhost:8080/api';

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options?.headers ?? {}),
    },
    ...options,
  });

  if (!response.ok) {
    const body = await response.text();
    throw new Error(body || 'Erro ao comunicar com a API');
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const api = {
  listDataCenters: () => request<any[]>('/data-centers'),
  listBastidores: (dataCenter: string) => request<any[]>(`/data-centers/${encodeURIComponent(dataCenter)}/bastidores`),
  getBastidor: (dataCenter: string, bastidor: string) => request<any>(`/data-centers/${encodeURIComponent(dataCenter)}/bastidores/${encodeURIComponent(bastidor)}`),
  listEquipamentos: (dataCenter: string, bastidor: string) => request<any[]>(`/data-centers/${encodeURIComponent(dataCenter)}/bastidores/${encodeURIComponent(bastidor)}/equipamentos`),
  addEquipamento: (dataCenter: string, bastidor: string, nome: string) => request<any>(`/data-centers/${encodeURIComponent(dataCenter)}/bastidores/${encodeURIComponent(bastidor)}/equipamentos`, {
    method: 'POST',
    body: JSON.stringify(nome),
  }),
  deleteEquipamento: (dataCenter: string, bastidor: string, nome: string) => request<void>(`/data-centers/${encodeURIComponent(dataCenter)}/bastidores/${encodeURIComponent(bastidor)}/equipamentos/${encodeURIComponent(nome)}`, {
    method: 'DELETE',
  }),
};
