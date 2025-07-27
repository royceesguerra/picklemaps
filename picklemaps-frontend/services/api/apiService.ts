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

// API Service interface
export interface ApiService {
  // Court endpoints
  searchCourts(params: CourtSearchParams): Promise<PaginatedResponse<CourtSummary>>;

  getCourt(courtId: string): Promise<Court>;

  createCourt(request: CreateCourtRequest): Promise<Court>;

  updateCourt(courtId: string, request: UpdateCourtRequest): Promise<void>;

  deleteCourt(courtId: string): Promise<void>;

  // Review endpoints
  getCourtReviews(
    courtId: string,
    sort?: 'datePosted,desc' | 'datePosted,asc' | 'rating,desc' | 'rating,asc',
    page?: number,
    size?: number,
  ): Promise<PaginatedResponse<Review>>;
  getCourtReview(courtId: string, reviewId: string): Promise<Review>;
  createReview(courtId: string, request: CreateReviewRequest): Promise<Review>;
  updateReview(courtId: string, reviewId: string, request: UpdateReviewRequest): Promise<void>;

  deleteReview(courtId: string, reviewId: string): Promise<void>;

  // Photo endpoint
  uploadPhoto(file: File, caption?: string): Promise<Photo>;
}
