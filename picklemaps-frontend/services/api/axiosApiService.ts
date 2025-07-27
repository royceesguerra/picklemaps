'use client';

import {
  CreateCourtRequest,
  CreateReviewRequest,
  PaginatedResponse,
  Photo,
  Court,
  CourtSearchParams,
  CourtSummary,
  Review,
  UpdateCourtRequest,
  UpdateReviewRequest,
} from '@/domain/domain';
import axios, {
  AxiosError,
  AxiosHeaders,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios';
import { useAuth } from 'react-oidc-context';
import { ApiService } from './apiService';

export class AxiosApiService implements ApiService {
  private api: AxiosInstance;
  private auth: ReturnType<typeof useAuth>;

  constructor(baseUrl: string, auth: ReturnType<typeof useAuth>) {
    this.auth = auth;
    this.api = axios.create({
      baseURL: baseUrl,
      headers: {
        'Content-Type': 'application/json',
      },
      withCredentials: true,
    });

    this.api.defaults.xsrfHeaderName = undefined;
    this.setupAuthInterceptor();
  }

  private setupAuthInterceptor() {
    this.api.interceptors.request.use(
      async (config: InternalAxiosRequestConfig) => {
        const headers = new AxiosHeaders(config.headers);

        if (this.auth.isAuthenticated) {
          // Check if token needs refresh
          const expiresAt = this.auth.user?.expires_at;
          const isExpiringSoon = expiresAt && expiresAt * 1000 - 60000 < Date.now();

          if (isExpiringSoon) {
            try {
              await this.auth.signinSilent();
            } catch (error) {
              console.error('Token refresh failed:', error);
              // Continue with existing token if refresh fails
            }
          }

          headers.setAuthorization(`Bearer ${this.auth.user?.access_token}`);
        }

        config.headers = headers;
        return config;
      },
      (error: AxiosError) => {
        return Promise.reject(error);
      },
    );

    this.api.interceptors.response.use(
      response => response,
      async (error: AxiosError) => {
        if (error.response?.status === 401) {
          try {
            await this.auth.signinSilent();
            // Retry the original request
            if (error.config) {
              const headers = new AxiosHeaders(error.config.headers);
              headers.setAuthorization(`Bearer ${this.auth.user?.access_token}`);
              error.config.headers = headers;
              return this.api.request(error.config);
            }
          } catch (refreshError) {
            // If silent refresh fails, redirect to login
            await this.auth.signinRedirect();
          }
        }
        return Promise.reject(error);
      },
    );
  }

  // Court endpoints implementation
  public async searchCourts(params: CourtSearchParams): Promise<PaginatedResponse<CourtSummary>> {
    const response: AxiosResponse<PaginatedResponse<CourtSummary>> = await this.api.get('/courts', {
      params,
    });
    return response.data;
  }

  public async getCourt(courtId: string): Promise<Court> {
    const response: AxiosResponse<Court> = await this.api.get(`/courts/${courtId}`);
    return response.data;
  }

  public async createCourt(request: CreateCourtRequest): Promise<Court> {
    const response: AxiosResponse<Court> = await this.api.post('/courts', request);
    return response.data;
  }

  public async updateCourt(courtId: string, request: UpdateCourtRequest): Promise<void> {
    await this.api.put(`/courts/${courtId}`, request);
  }

  public async deleteCourt(courtId: string): Promise<void> {
    await this.api.delete(`/courts/${courtId}`);
  }

  // Review endpoints implementation
  public async getCourtReviews(
    courtId: string,
    sort?: 'datePosted,desc' | 'datePosted,asc' | 'rating,desc' | 'rating,asc',
    page?: number,
    size?: number,
  ): Promise<PaginatedResponse<Review>> {
    const response: AxiosResponse<PaginatedResponse<Review>> = await this.api.get(
      `/courts/${courtId}/reviews`,
      {
        params: { sort, page, size },
      },
    );
    return response.data;
  }

  public async getCourtReview(courtId: string, reviewId: string): Promise<Review> {
    const response: AxiosResponse<Review> = await this.api.get(
      `/courts/${courtId}/reviews/${reviewId}`,
    );
    return response.data;
  }

  public async createReview(courtId: string, request: CreateReviewRequest): Promise<Review> {
    const response: AxiosResponse<Review> = await this.api.post(
      `/courts/${courtId}/reviews`,
      request,
    );
    return response.data;
  }

  public async updateReview(
    courtId: string,
    reviewId: string,
    request: UpdateReviewRequest,
  ): Promise<void> {
    await this.api.put(`/courts/${courtId}/reviews/${reviewId}`, request);
  }

  public async deleteReview(courtId: string, reviewId: string): Promise<void> {
    await this.api.delete(`/courts/${courtId}/reviews/${reviewId}`);
  }

  // Photo endpoint implementation
  public async uploadPhoto(file: File, caption?: string): Promise<Photo> {
    const formData = new FormData();
    formData.append('file', file);
    if (caption) {
      formData.append('caption', caption);
    }

    const response: AxiosResponse<Photo> = await this.api.post('/photos', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  }
}
