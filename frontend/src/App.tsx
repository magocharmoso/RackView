import { useEffect, useState } from 'react';
import { BrowserRouter, Link, Route, Routes, useNavigate, useParams } from 'react-router-dom';
import type { ReactNode } from 'react';
import { api } from './lib/api';
import type { Bastidor, DataCenter, Equipamento } from './types';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-slate-950 text-slate-100">
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/datacenters" element={<DataCentersPage />} />
          <Route path="/datacenters/:dataCenter/bastidores" element={<BastidoresPage />} />
          <Route path="/datacenters/:dataCenter/bastidores/:bastidor" element={<BastidorPage />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

function HomePage() {
  const navigate = useNavigate();

  useEffect(() => {
    navigate('/datacenters');
  }, [navigate]);

  return <div className="p-8 text-slate-300">A carregar...</div>;
}

function DataCentersPage() {
  const [dataCenters, setDataCenters] = useState<DataCenter[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        const result = await api.listDataCenters();
        setDataCenters(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Erro ao carregar data centers');
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, []);

  if (loading) return <PageShell title="Datacenters"><LoadingState /></PageShell>;
  if (error) return <PageShell title="Datacenters"><ErrorState message={error} /></PageShell>;

  return (
    <PageShell title="Datacenters">
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {dataCenters.map((dataCenter) => (
          <Link
            key={dataCenter.nome}
            to={`/datacenters/${encodeURIComponent(dataCenter.nome)}/bastidores`}
            className="group rounded-2xl border border-slate-700 bg-slate-900 p-5 transition hover:border-cyan-400 hover:bg-slate-800"
          >
            <p className="text-xs uppercase tracking-[0.2em] text-cyan-300">Data Center</p>
            <h2 className="mt-3 text-2xl font-semibold text-white">{dataCenter.nome}</h2>
            <p className="mt-2 text-sm text-slate-400">{dataCenter.bastidores?.length ?? 0} bastidores</p>
          </Link>
        ))}
      </div>
    </PageShell>
  );
}

function BastidoresPage() {
  const { dataCenter } = useParams();
  const [bastidores, setBastidores] = useState<Bastidor[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!dataCenter) return;

    const load = async () => {
      try {
        setLoading(true);
        const result = await api.listBastidores(dataCenter);
        setBastidores(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Erro ao carregar bastidores');
      } finally {
        setLoading(false);
      }
    };

    void load();
  }, [dataCenter]);

  if (!dataCenter) return <PageShell title="Bastidores"><ErrorState message="Data center inválido" /></PageShell>;
  if (loading) return <PageShell title={`Bastidores · ${dataCenter}`}><LoadingState /></PageShell>;
  if (error) return <PageShell title={`Bastidores · ${dataCenter}`}><ErrorState message={error} /></PageShell>;

  return (
    <PageShell title={`Bastidores · ${dataCenter}`} backTo="/datacenters">
      <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
        {bastidores.map((bastidor) => (
          <Link
            key={bastidor.nome}
            to={`/datacenters/${encodeURIComponent(dataCenter)}/bastidores/${encodeURIComponent(bastidor.nome)}`}
            className="rounded-2xl border border-slate-700 bg-slate-900 p-5"
          >
            <div className="flex items-center justify-between gap-3">
              <h2 className="text-xl font-semibold text-white">{bastidor.nome}</h2>
              <span className="rounded-full bg-cyan-500/10 px-2 py-1 text-xs font-medium text-cyan-200">
                {bastidor.equipamentos?.length ?? 0}
              </span>
            </div>
            {bastidor.nickname && <p className="mt-2 text-sm text-slate-400">{bastidor.nickname}</p>}
          </Link>
        ))}
      </div>
    </PageShell>
  );
}

function BastidorPage() {
  const { dataCenter, bastidor } = useParams();
  const [rack, setRack] = useState<Bastidor | null>(null);
  const [equipamentos, setEquipamentos] = useState<Equipamento[]>([]);
  const [nomeEquipamento, setNomeEquipamento] = useState('');
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const loadRack = async () => {
    if (!dataCenter || !bastidor) return;

    try {
      setLoading(true);
      const bastidorData = await api.getBastidor(dataCenter, bastidor);
      const eqs = await api.listEquipamentos(dataCenter, bastidor);
      setRack(bastidorData);
      setEquipamentos(eqs);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao carregar bastidor');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadRack();
  }, [dataCenter, bastidor]);

  const addEquipamento = async () => {
    if (!dataCenter || !bastidor || !nomeEquipamento.trim()) return;

    try {
      setSubmitting(true);
      setError(null);
      await api.addEquipamento(dataCenter, bastidor, nomeEquipamento.trim());
      setNomeEquipamento('');
      await loadRack();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao adicionar equipamento');
    } finally {
      setSubmitting(false);
    }
  };

  const removeEquipamento = async (nome: string) => {
    if (!dataCenter || !bastidor) return;

    try {
      setError(null);
      await api.deleteEquipamento(dataCenter, bastidor, nome);
      await loadRack();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao remover equipamento');
    }
  };

  if (!dataCenter || !bastidor) return <PageShell title="Bastidor"><ErrorState message="Bastidor inválido" /></PageShell>;
  if (loading) return <PageShell title={`Bastidor · ${bastidor}`} backTo={`/datacenters/${encodeURIComponent(dataCenter)}/bastidores`}><LoadingState /></PageShell>;
  if (error && !rack) return <PageShell title={`Bastidor · ${bastidor}`} backTo={`/datacenters/${encodeURIComponent(dataCenter)}/bastidores`}><ErrorState message={error} /></PageShell>;

  return (
    <PageShell title={`Bastidor · ${bastidor}`} backTo={`/datacenters/${encodeURIComponent(dataCenter)}/bastidores`}>
      <div className="mb-6 rounded-2xl border border-slate-700 bg-slate-900 p-5">
        <h3 className="text-lg font-semibold text-white">{rack?.nome ?? bastidor}</h3>
        {rack?.nickname && <p className="mt-2 text-sm text-slate-400">{rack.nickname}</p>}
      </div>

      <div className="mb-8 flex flex-col gap-3 sm:flex-row">
        <input
          value={nomeEquipamento}
          onChange={(event) => setNomeEquipamento(event.target.value)}
          placeholder="Ex: GT-01 / SW-04 / FW-01"
          className="flex-1 rounded-xl border border-slate-700 bg-slate-900 px-4 py-3 text-white placeholder:text-slate-500 focus:border-cyan-400 focus:outline-none"
        />
        <button
          disabled={submitting || !nomeEquipamento.trim()}
          onClick={addEquipamento}
          className="rounded-xl bg-cyan-500 px-5 py-3 font-medium text-slate-950 transition hover:bg-cyan-400 disabled:cursor-not-allowed disabled:opacity-50"
        >
          {submitting ? 'A adicionar...' : 'Adicionar equipamento'}
        </button>
      </div>

      {error && <div className="mb-6 rounded-xl border border-red-500/40 bg-red-500/10 p-3 text-sm text-red-200">{error}</div>}

      <div className="overflow-hidden rounded-2xl border border-slate-700">
        <table className="min-w-full divide-y divide-slate-700 text-left">
          <thead className="bg-slate-900">
            <tr>
              <th className="px-4 py-3 text-sm font-medium text-slate-300">Equipamento</th>
              <th className="px-4 py-3 text-sm font-medium text-slate-300">Cor</th>
              <th className="px-4 py-3 text-sm font-medium text-slate-300 text-right">Ações</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-800 bg-slate-950">
            {equipamentos.length === 0 ? (
              <tr>
                <td colSpan={3} className="px-4 py-8 text-center text-slate-400">Nenhum equipamento neste bastidor.</td>
              </tr>
            ) : (
              equipamentos.map((equipamento) => (
                <tr key={equipamento.name}>
                  <td className="px-4 py-3 text-white">{equipamento.name}</td>
                  <td className="px-4 py-3 text-slate-300">{equipamento.cor ?? 'N/D'}</td>
                  <td className="px-4 py-3 text-right">
                    <button
                      onClick={() => void removeEquipamento(equipamento.name)}
                      className="rounded-lg border border-red-500/50 bg-red-500/10 px-3 py-1.5 text-sm text-red-200 hover:bg-red-500/20"
                    >
                      Remover
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </PageShell>
  );
}

function PageShell({ title, backTo, children }: { title: string; backTo?: string; children: ReactNode }) {
  return (
    <div className="mx-auto max-w-6xl p-6 md:p-10">
      <div className="mb-6 flex items-center justify-between gap-3">
        <div>
          {backTo && (
            <Link to={backTo} className="mb-2 inline-block text-sm text-cyan-300 hover:text-cyan-200">
              ← Voltar
            </Link>
          )}
          <h1 className="text-3xl font-bold text-white">{title}</h1>
        </div>
      </div>
      {children}
    </div>
  );
}

function LoadingState() {
  return <div className="rounded-2xl border border-slate-700 bg-slate-900 p-5 text-slate-300">A carregar...</div>;
}

function ErrorState({ message }: { message: string }) {
  return <div className="rounded-2xl border border-red-500/50 bg-red-500/10 p-5 text-red-200">{message}</div>;
}

export default App;
