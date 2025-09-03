// 공통 fetch 래퍼: 쿠키 전송 + JSON 파싱 일관화
export type ApiFetchInit = RequestInit & { json?: unknown };

export async function apiFetch<T = unknown>(input: string | URL | Request, init?: ApiFetchInit): Promise<T> {
  const headers = new Headers(init?.headers ?? {});
  if (init?.json !== undefined) {
    headers.set('Content-Type', 'application/json');
  }
  if (!headers.has('Accept')) headers.set('Accept', 'application/json');

  const res = await fetch(input, {
    ...init,
    headers,
    credentials: 'include',
    body: init?.json !== undefined ? JSON.stringify(init.json) : init?.body,
  });

  const text = await res.text();
  const data: unknown = text ? JSON.parse(text) : undefined;

  if (!res.ok) {
    let msg = `HTTP ${res.status}`;
    if (data && typeof data === 'object' && 'message' in data) {
      const m = (data as { message?: unknown }).message;
      if (typeof m === 'string' && m) msg = m;
    }
    throw new Error(msg);
  }
  return data as T;
}
